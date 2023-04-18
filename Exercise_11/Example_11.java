import java.util.LinkedList;
import java.util.List;
import Aspects.SharedAspect;

public class Example_11 {
	private void go() {
		// 1 MODO: usando questo tipo di oggetto alla fine non avremo effettivamente
		// 10000 come risultato,
		// ma un po meno perche' non ci garantisce la mutua escusione di per se
		// List<Integer> list = new LinkedList<>();

		// 2 MODO: utilizzando questo tipo di oggetto alla fine otterremo 10000 come
		// risultato perche' lo SharedAspect
		// ci garantisce condivisione in mutua escusione
		List<Integer> list = SharedAspect.attach(new LinkedList<>());

		// il primo for serve a creare i thread
		for (int i = 0; i < 10; ++i) {
			// id non cambia, perchè viene creato e distrutto a tutte le iterazioni
			int id = i;

			new Thread(() -> {
				// il secondo for serve a creare i job per ogni thread di consequenza dato che
				// ogni thread mi crea 10k job
				// in totale avrò 100k job perchè avrò 10*10k = 100k
				for (int c = 0; c < 10000; ++c)
					list.add(c);

				// dobbiamo mettere id perchè i non è final e quindi non è visibile dalla Inner Class
				System.out.println("Thread " + id + " done");
			}).start();
		}

		try {
			// devo creare un sleep per dare il tempo ad ogni thread di finire l'esecuzione
			Thread.sleep(1000);

			System.out.println(list.size());
		} catch (Throwable throwable) {
			// Blank
		}
	}

	public static void main(String[] args) {
		new Example_11().go();
	}
}
/*
 * Il codice fornito mostra un esempio di utilizzo della classe `Thread` per
 * eseguire più thread contemporaneamente. Il metodo `go` crea una lista vuota
 * di interi (`LinkedList<Integer>`) e quindi inizia a eseguire 10 thread. Ogni
 * thread aggiunge 10000 interi alla lista. Il motivo per cui vengono creati 10
 * thread diversi è per dimostrare la concorrenza. Se solo un thread fosse stato
 * utilizzato, l'aggiunta di 10000 interi alla lista sarebbe stata eseguita in
 * modo sequenziale, un elemento alla volta. Invece, l'utilizzo di 10 thread
 * diversi significa che gli elementi vengono aggiunti in modo concorrente, il
 * che può portare a problemi di sincronizzazione e prestazioni. Dopo aver
 * eseguito tutti i thread, il codice attende per 5 secondi (utilizzando il
 * metodo `Thread.sleep()`), quindi stampa la dimensione della lista. Poiché il
 * metodo `add()` della lista non è thread-safe, il risultato potrebbe non
 * essere quello atteso. In altre parole, il numero di elementi nella lista
 * potrebbe non essere esattamente 100000, ma potrebbe essere maggiore o minore
 * a seconda di come i thread sono stati eseguiti. Il metodo `main` crea
 * un'istanza della classe `Example11` e richiama il metodo `go` per avviare
 * l'esecuzione dei thread.
 */