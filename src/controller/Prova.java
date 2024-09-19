package controller;

import java.util.concurrent.Semaphore;
import java.util.Random;
public class Prova extends Thread {

	private Semaphore semaforoTiro;
	private int[] atleta = new int[4];
	private static int[][] rank = new int[25][2];
	private static int index = 0;
	
	/* atleta[0] - ID
	 * atleta[1] - pontCorrida
	 * atleta[2] - pontTiro
	 * atleta[3] - pontCiclismo
	 */
	//velocidade da corrida - 20-25m a cada 30ms
	//velocidade do ciclismo - 30-40m a cada 40ms
	
	private final int distCorrida = 3000;
	private final int distCiclismo = 5000;
	private static int pontCorrida = 250;
	private static int pontCiclismo = 250;
	
	public Prova(Semaphore semaforo, int id) {
		this.semaforoTiro = semaforo;
		this.atleta[0] = id;
	}
	
	private void corrida() {
		Random rand = new Random();
		System.out.println("O atleta " + atleta[0] + " está correndo");
		while(atleta[1] < distCorrida) {
			if(atleta[1] >= 2975) {
				int comp = (distCorrida - atleta[1]); 
				atleta[1] += comp;
			}else {
				atleta[1] += rand.nextInt(20,26);
			}
			
			try {
				sleep(31);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("O atleta " + atleta[0] + " chegou ao fim da etapa de corrida e recebeu " + pontCorrida + " pontos" );
		atleta[1] = pontCorrida;
		pontCorrida -= 10;
	}
	
	private void tiro() {
		Random rand = new Random();
		
		for(int i = 0; i < 3; i++) {
			int tempoTiro = rand.nextInt(500,3001);
			
			try {
				sleep(tempoTiro);
				int pontTiro = rand.nextInt(0,11);
				atleta[2] += pontTiro;
				System.out.println("O atleta " + atleta[0] + " atirou em " + (tempoTiro/Math.pow(10, 3)) + " segundos e recebeu " + pontTiro + " pontos" );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("O atleta " + atleta[0] + " terminou a etapa de tiro e acumulou o total de " + atleta[2] + " pontos");
	}
	
	private void ciclismo() {
		Random rand = new Random();
		System.out.println("O atleta " + atleta[0] + " está pedalando");
		while(atleta[3] < distCiclismo) {
			if(atleta[3] >= 4960) {
				int comp = (distCiclismo - atleta[3]); 
				atleta[3] += comp;
			}else {
				atleta[3] += rand.nextInt(30,41);
			}
			
			try {
				sleep(41);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("O atleta " + atleta[0] + " chegou ao fim da etapa de ciclismo e recebeu " + pontCiclismo + " pontos" );
		atleta[3] = pontCiclismo;
		pontCiclismo -= 10;
		
		int[] vetorFinalizacao = {atleta[0], (atleta[1]+atleta[2]+atleta[3]) };
		ranking(vetorFinalizacao);
	}
	
	private void ranking(int[] vetorFinalizacao) {
		rank[index] = vetorFinalizacao;
		++index;
		
		if(index == 25) {
			rank = ordena(rank);
			mostraRank(rank);
		}
	}
	
	@Override
	public void run() {
		corrida();
		
		try {
			semaforoTiro.acquire();
			tiro();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			semaforoTiro.release();
		}
		
		ciclismo();
	}

	private void mostraRank(int[][] rank) {
		for(int i = 0; i < 25; i++) {
			System.out.println("O atleta " + rank[i][0] + " terminou em " + (i+1) + "o. com o total de " + rank[i][1] + " pontos");
		}
	}
	
	private int[][] ordena(int[][] rank) {
		//rank[0] - id / rank[1] - pontuacao
		
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 24; j++) {
				int[] aux = new int[2];
				if(rank[j][1] < rank[j+1][1]) {
					
					aux = rank[j+1];
					rank[j+1] = rank[j];
					rank[j] = aux;
				}
			}
		}
		return rank;
	}
}
