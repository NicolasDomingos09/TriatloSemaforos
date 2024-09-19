package view;

import controller.Prova;
import java.util.concurrent.Semaphore;

public class Main {

	public static void main(String[] args) {

		Semaphore semaforo = new Semaphore(5);
		
		for(int i = 0; i < 25; i++) {
			Thread atleta = new Prova(semaforo, (i+1));
			atleta.start();
		}
	}

}
