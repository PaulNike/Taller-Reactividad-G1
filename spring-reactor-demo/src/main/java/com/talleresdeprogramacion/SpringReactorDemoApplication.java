package com.talleresdeprogramacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

@SpringBootApplication
public class SpringReactorDemoApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory
            .getLogger(SpringReactorDemoApplication.class);

    private static List<String> comidas = new ArrayList<>();
    public void createMono(){
        Mono<String> m1 = Mono.just("Hello World");
        //Mono.just("Hello World").subscribe( dato -> System.out.println(Thread.currentThread().getName() + " - " + dato));
        //Mono.just("Hello World").subscribe(log::info); // SLF4J/ LogBack
        m1.subscribe(log::info);
    }

    public void createFlux(){
        //Puede contener hasta 10 elementos, luego pasa a ser ineficiente
        Flux.just("Mazda","Toyota","Nissan").subscribe(dato -> System.out.println(Thread.currentThread().getName() + " - " + dato));
        //Crea un flux a partir de cualquier Iterable, LIST - SET - Queue
        Flux.fromIterable(comidas).subscribe(dato -> System.out.println(Thread.currentThread().getName() + " - " + dato));
        //Crea un flux de numeros enteros en un rango determinado
        Flux.range(1,5).subscribe(dato -> System.out.println(Thread.currentThread().getName() + " - " + dato));
        Flux.range(1,5).subscribe(dato -> log.info(Thread.currentThread().getName() + " - " + dato));
        Flux<String> vacio = Flux.empty();
        Flux<String> never = Flux.never();
        //Flux.range(1,5).subscribeOn(Schedulers.immediate()).subscribe(log::info); --> REVISAR

    }

    public void m1doOnNext(){
        Flux<String> fx1 = Flux.fromIterable(comidas);
        fx1.doOnNext(dato -> log.info("Elemento: "+ dato));

        Flux.just("RENZO", "IVAN", "EDUARDO").
                doOnNext(dato -> System.out.println("Antes de suscribirse: "+ dato))
                .subscribe(dato -> System.out.println("Suscriptor recibió: "+ dato));
    }

    public void m2map(){
        Flux<String> fx2 = Flux.fromIterable(comidas);
        fx2.doOnNext(string -> log.info("Valores antes de tranformarse: " + string))
                .map(String::toUpperCase)  //.map(string -> string.toUpperCase())
                .subscribe(string -> log.info("Transformado: " + string));

        Flux<String> numero = Flux.just("1","2","3");
        Flux<Integer> enteros = numero.map(Integer::parseInt).map(x -> x + 1);
        enteros.subscribe(dat -> log.info("datos emitidos: "+ dat));

    }

    public void m3flatMap(){
        Mono.just("Paul").map(x -> 10).subscribe(e -> log.info("Data: "+ e));
        Mono.just("Paul").map(x -> Mono.just(34)).subscribe(e -> log.info("Data: "+ e));
        //Mono<Mono<T>>
        Mono.just("Paul").flatMap(x -> Mono.just(34)).subscribe(e -> log.info("Data: "+ e));
        // .map(x -> T) -> SINCRONO -> Devuelve Mono<T> -> Lo usas solo para
        // transformaciones de valores directas

        // .flatMap(x -> Mono<T>) -> ASINCRONO -> Devuelve Mono<T> aplana Mono<Mono<T>> -> Lo usas solo para
        // llamadas a BD, Apis o transformaciones asincronas

        Flux<Integer> flujoOriginal = Flux.just(1,2,3,4,5)
                .doOnNext(num -> System.out.println("Antes de Transformar: "+ num))
                .map(num -> num*10);

        //Flux<Integer> flujoModificado = flujoOriginal.map(num -> num*10);

        flujoOriginal.subscribe(num -> System.out.println("Original: "+ num));
        //flujoModificado.subscribe(num -> System.out.println("MOdificado: "+ num));

    }

    public void m4delayElements() throws InterruptedException {
        Flux.range(0,20)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(x -> log.info("Elemento: "+ x))
                .subscribe();

        Thread.sleep(24000);
    }

    public void m5zipWith(){
        List<String> clientes = new ArrayList<>();
        clientes.add("Angel");
        clientes.add("Roy");
        clientes.add("Leonil");
        //clientes.add("Renzo");
        //clientes.add("Ruben");

        Flux<String> fx1 = Flux.fromIterable(clientes);
        Flux<String> fx2 = Flux.fromIterable(comidas);

        fx1.zipWith(fx2, (c, d) -> c + " - " + d).subscribe(log::info);

        Flux<String> nombres = Flux.just("Eduardo", "Paul", "Angel");
        Flux<Integer> edades = Flux.just(25,28,29,24,23);
        Flux<String> ciudad = Flux.just("Lima", "Cajamarca", "Chiclayo");

        Flux.zip(nombres,edades, ciudad)
                .map(dat -> dat.getT1() + " tiene "+ dat.getT2() + " años y vive en: "+ dat.getT3())
                .subscribe(log::info);

    }

    public void m6merge(){
        List<String> clientes = new ArrayList<>();
        clientes.add("Cliente 1");
        clientes.add("Cliente 2");

        Flux<String> fx1 = Flux.fromIterable(clientes);
        Flux<String> fx2 = Flux.fromIterable(comidas);
        Mono<String> m1 = Mono.just("Taller reactivo");

        Flux.merge(fx1,fx2, m1).subscribe(log::info);

    }
    public void m7merge() throws InterruptedException {
        Flux<String> clientes = Flux.just("Cliente A", "Cliente B")
                .delayElements(Duration.ofMillis(500));
        Flux<String> productos = Flux.just("Producto 1", "Producto 2", "Producto 3")
                .delayElements(Duration.ofMillis(700));

        Flux.merge(clientes, productos).subscribe(log::info);

        Thread.sleep(5000);

    }
    public void m8merge() throws InterruptedException {

        Mono<String> saludo = Mono.just("Bienvenido a Reactor!");
        Flux<Long> numeros = Flux.interval(Duration.ofMillis(500)).map(n -> n + 1).take(10);
        Flux.merge(saludo, numeros.map(String::valueOf)).subscribe(log::info);

        Thread.sleep(10000);

    }

    public void m9filter(){
        Flux<String> fx1 = Flux.fromIterable(comidas);
        Predicate<String> predicate = e -> e.startsWith("C");
        Predicate<String> caracteres = e -> e.length() <= 3;

        fx1.filter(predicate.and(caracteres))
                .map(String::toUpperCase)
                .subscribe(log::info);
    }
    public void m10filterNegate(){
        Flux<String> fx1 = Flux.fromIterable(comidas);
        Predicate<String> predicate = e -> e.startsWith("C");


        fx1.filter(predicate.negate())
                .map(String::toUpperCase)
                .subscribe(log::info);
    }

    public void m11take(){
        Flux<String> fx1 = Flux.fromIterable(comidas);
        fx1.take(2).subscribe(log::info);

    }
    public void m12takeDelay() throws InterruptedException {
        Flux<String> fx1 = Flux.just("Ceviche", "Arroz con Pollo", "Causa", "Lomito", "Parrilla", "Bistec", "Pollo a la brasa")
                .delayElements(Duration.ofMillis(1000));

        fx1.take(3).subscribe(log::info);

        Thread.sleep(6000);

    }
    public void m13takeUntil(){
        Flux<String> fx1 = Flux.just("Ceviche", "Arroz con Pollo", "Causa",
                "Lomito", "Parrilla", "Bistec", "Pollo a la brasa");
        fx1.takeUntil(e -> e.equalsIgnoreCase("bIsTec"))
                .subscribe(log::info);

    }

    public void m14TakeLast(){
        Flux<String> fx1 = Flux.fromIterable(comidas);
        fx1.takeLast(2).subscribe(log::info);

    }

    public void m15TakeLast(){
        List<LocalDate> fechas = List.of(
                LocalDate.of(2022,5,10),
                LocalDate.of(2023,8,12),
                LocalDate.of(2025,1,30),
                LocalDate.of(2024,10,30),
                LocalDate.of(2020,4,6)
        );
        Flux.fromIterable(fechas)
                .sort(Comparator.reverseOrder()) //Comparator.reverseOrder() Orden DESC
                .takeLast(2)
                .subscribe(e -> log.info("fecha: " +e));

    }


    public void m16DefaultEmpty(){
        comidas = new ArrayList<>();
        Flux<String> fx1 = Flux.fromIterable(comidas);
        fx1.map(String::toUpperCase)
                .defaultIfEmpty("empty flux")
                .subscribe(log::info);
    }

    public void m17Empty(){
       Flux<String> fluxVacio = Flux.empty();
       Flux<String> fluxError = Flux.error(new RuntimeException("ERROR CRITICO!"));
       //Flux<String> fluxDatos = Flux.just("Dato 1", "Dato 2");

       //Aplicando defaultIfEmpty()
        fluxVacio.defaultIfEmpty("No hay Datos").subscribe(log::info);
       // switchIfEmpty
        fluxVacio.switchIfEmpty(Flux.just("Alternativa 1", "Alternativa 2"))
                .subscribe(log::info);
        //OnError
        fluxError.onErrorReturn("Error Detectado").subscribe(log::info);
    }

    public void m18Error(){
        Flux<String> fx1 = Flux.fromIterable(comidas);
        fx1.doOnNext(e -> {
            throw new ArithmeticException("ERROR OPERATION");
        })
                //.onErrorMap(e -> new Exception(e.getMessage()))
                .onErrorReturn("ERROR, VUELVE A INTENTAR")
                // ERROR OPERATION -> Exception(ERROR OPERATION);
                .subscribe(log::info);
    }

    public void m18ErrorResume(){
        Flux.just("A","B","C")
                .map(valor -> {
                    if(valor.equals("B")){
                        throw new IllegalStateException("Error en B");
                    }
                    return valor;
                })
                //.onErrorResume(e -> Flux.just("D","E","F"))
                .onErrorContinue((e, valor )
                        -> log.info("Error en: " + valor + " - " + e.getMessage()))
                .subscribe(log::info);
    }
    public void m19ErrorResume(){
      Flux.just(10,5,0,20,2)
              .map(num -> 100/num) //Division
              .onErrorContinue((error, valor) ->
                      log.info("Error en: "+ valor + " - " + error.getMessage()))
              .subscribe(response -> log.info("valores" + response));
    }

    public void m20ErrorResume(){
        Flux.just(10,5,0,20,2,-1,4)
                .map(n -> {
                    if (n == 0) throw new ArithmeticException("División por 0");
                    if (n < 0) throw new IllegalStateException("Numero negatico no permitido");
                    return 100/n;
                })
                .onErrorContinue((error, valor) -> {
                    if (error instanceof ArithmeticException){
                        log.info("Error manejado : " + valor + " - " + error.getMessage());
                    }else{
                        throw new RuntimeException(error);
                    }
                })
                .subscribe(integer -> log.info("Valor : "+ integer),
                        error -> log.info("Flujo detenido por error : " + error));
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        comidas.add("Ceviche");
        comidas.add("Lomo Saltado");
        comidas.add("Pachamanca");
        comidas.add("Cuy");
        comidas.add("Arroz con Pollo");
        m20ErrorResume();
    }

}
