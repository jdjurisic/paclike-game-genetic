package rs.edu.raf.mtomic.paclike;

import rs.edu.raf.mtomic.Chromosome;
import rs.edu.raf.mtomic.paclike.agent.player.PlayerOne;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Test klasa:
 * <p>
 * Ovde možete ubaciti proizvoljni sistem za optimizaciju.
 * <p>
 * Zadatak je naslediti klasu Player i implementirati metodu generateNextMove,
 * koja će za svaki poziv vratiti jednu od sledećih metoda:
 * this::goUp, this::goLeft, this::goDown, this::goRight
 * te na taj način upravljati igračem u lavirintu.
 * <p>
 * Može se i (umesto nove klase) izmeniti klasa PlayerOne, koja sada samo ide levo.
 * <p>
 * U metodi se smeju koristiti svi dostupni geteri, ali ne smeju se
 * koristiti metode koje na bilo koji način menjaju stanje protivničkih
 * agenata ili stanje igre.
 * <p>
 * Matrica fields iz gameState: prvi indeks je kolona (X), drugi je vrsta (Y)
 * <p>
 * Zadatak: koristiti genetski algoritam za optimizaciju parametara
 * koji mogu odlučivati o sledećem potezu igrača. Generisanje poteza se
 * vrši na svaki frejm. Cilj je pokupiti svih 244 tačkica iz lavirinta.
 * Ako igrač naleti na protivnika, igra se prekida.
 * <p>
 * Savet: pogledajte kako Ghost agenti odlučuju o tome kada treba napraviti
 * skretanje (mada oni imaju jednostavna ponašanja) u njihovoj metodi Ghost::playMove.
 * <p>
 * Takođe, u implementacijama njihove metode calculateBest mogu se videti
 * primeri korišćenja GameState, iz koga se čitaju svi parametri.
 * <p>
 * Konačno stanje igre generiše se pokretanjem igre preko konstruktora i
 * pozivom join(), pa onda getTotalPoints().
 * <p>
 * Igrač se inicijalizuje GameState-om null, a PacLike će obezbediti
 * odgovarajuće stanje.
 * <p>
 * Ukoliko želite da pogledate simulaciju igre, promenite polje render
 * u klasi PacLike na true, a fps podesite po želji (ostalo ne treba
 * dirati).
 * <p>
 * Ograničenja:
 * - Svi parametri koji se koriste u generateNextMove() moraju biti
 * ili nepromenjeni (automatski generisani i menjani od strane igre),
 * ili inicijalizovani pomoću genetskog algoritma, ili eventualno
 * ako dodajete nove promenljive u klasu inicijalizovani u konstruktoru.
 * <p>
 * - Kalkulacije pomoću tih parametara i na osnovu onoga što igrač vidi
 * na osnovu GameState klase (i svih getera odatle i od objekata do
 * kojih odatle može da se dospe) su dozvoljene i poželjne; nije
 * dozvoljeno oslanjati se na unutrašnju logiku drugih agenata i hardkodovati
 * ponašanja ili šablone koji postoje za ovu igru (mada je engine
 * dosta promenjen u odnosu na original, iako liči, tako da je
 * većina šablona u suštini neupotrebljiva).
 **/
public class RunSimulation {

    public static void main(String[] args) {
        // searching for the best chromosome
        long startTime = System.nanoTime();
        ArrayList<Chromosome> population = new ArrayList<Chromosome>();
        Random random = new Random();

        for(int i=0; i<250;i++){
            population.add(new Chromosome(random.nextDouble()*5000,random.nextDouble()*5000,random.nextDouble()*5000,random.nextDouble()*5000,random.nextDouble()*5000));
        }

        Chromosome best_ever_chrom = null;
        int best_ever_total_points = 0;

        int t=0;
        int max_iter = 10;

        while(t < max_iter){
            for(int i=0; i<50;i++){
                population.add(new Chromosome(random.nextGaussian()*random.nextDouble()*5000,random.nextGaussian()*random.nextDouble()*5000,random.nextGaussian()*random.nextDouble()*5000,random.nextGaussian()*random.nextDouble()*5000,random.nextGaussian()*random.nextDouble()*5000));
            }
            while(population.size() < 600){
                Chromosome h1 = tournament(population,10);
                Chromosome h2 = tournament(population,10);
                ArrayList<Chromosome> crossoverChromosomes = crossover(h1,h2);
                population.add(mutate(crossoverChromosomes.get(0),0.25));
                population.add(mutate(crossoverChromosomes.get(1),0.25));
            }
            SortedMap<Integer,ArrayList<Chromosome>> sm = new TreeMap<Integer, ArrayList<Chromosome>>();
            for(Chromosome c:population){
                if(c.getScore() == 0){
                    PacLike pacLike = new PacLike(new PlayerOne(null,c),false);
                    try {
                        pacLike.join(200);
                        c.setScore(pacLike.getTotalPoints());
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                if(sm.containsKey(c.getScore())){
                    sm.get(c.getScore()).add(c);
                }else{
                    ArrayList a = new ArrayList<Chromosome>();
                    a.add(c);
                    sm.put(c.getScore(),a);
                }
            }

            if(sm.lastKey() > best_ever_total_points){
                best_ever_total_points = sm.lastKey();
                best_ever_chrom = sm.get(sm.lastKey()).get(0);
            }

            population.clear();
            while(population.size() < 250){
                for(Chromosome chr : (ArrayList<Chromosome>)sm.get(sm.lastKey())){
                    population.add(new Chromosome(chr.getCoef1(),chr.getCoef2(),chr.getCoef3(),chr.getCoef4(),chr.getClosestPellet()));
                }
                sm.remove(sm.lastKey());
            }
            if(population.size() > 250){
                population.subList(250,population.size()-1).clear();
            }

            System.out.println("Generation:"+t+"| Score:"+best_ever_total_points+" | "+best_ever_chrom.toString());
            t++;
            if(best_ever_total_points==244)break;
        }

        long stopTime = System.nanoTime();
        long convert = TimeUnit.SECONDS.convert(stopTime - startTime, TimeUnit.NANOSECONDS);
        System.out.println(convert + " seconds");

        // Best solution ever -> Chromosome(4004.644390730013,-55.14120393717613,-702.9602980503226,281.26908428888237,5000.0)
        // Score 243
        PacLike pacLike = new PacLike(new PlayerOne(null,new Chromosome(4004.644390730013,-55.14120393717613,-702.9602980503226,281.26908428888237,5000.0)),true);
        try {
            pacLike.join();
            System.out.println("Done: " + pacLike.getTotalPoints());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }


    static Chromosome tournament(ArrayList<Chromosome> pop,int size){
        Random rnd = new Random();
        ArrayList<Chromosome> tour = new ArrayList<>();

        for(int i=0;i<size;i++) tour.add(pop.get(rnd.nextInt(pop.size())));

        Chromosome best_chrom = null;
        int best_chrom_points = 0;

        for(Chromosome ch : tour){
            if(ch.getScore() == 0){
                PacLike pacLike = new PacLike(new PlayerOne(null,ch),false);
                try {
                    pacLike.join(200);
                    ch.setScore(pacLike.getTotalPoints());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            if(ch.getScore() > best_chrom_points){
                best_chrom_points = ch.getScore();
                best_chrom = ch;
            }
        }

    return best_chrom;
    }

    static ArrayList<Chromosome> crossover(Chromosome h1, Chromosome h2){
        Random rand = new Random();
        double beta = rand.nextGaussian();

        ArrayList<Chromosome> newChromosomes = new ArrayList<>();
        Chromosome chr1 = new Chromosome((beta*h1.getCoef1()+(1-beta)*h2.getCoef1()),
                (beta*h1.getCoef2()+(1-beta)*h2.getCoef2()),
                (beta*h1.getCoef3()+(1-beta)*h2.getCoef3()),
                (beta*h1.getCoef4()+(1-beta)*h2.getCoef4()),
                (beta*h1.getClosestPellet()+(1-beta)*h2.getClosestPellet()));

        Chromosome chr2 = new Chromosome((beta*h2.getCoef1()+(1-beta)*h1.getCoef1()),
                (beta*h2.getCoef2()+(1-beta)*h1.getCoef2()),
                (beta*h2.getCoef3()+(1-beta)*h1.getCoef3()),
                (beta*h2.getCoef4()+(1-beta)*h1.getCoef4()),
                (beta*h2.getClosestPellet()+(1-beta)*h1.getClosestPellet()));
        newChromosomes.add(chr1);
        newChromosomes.add(chr2);
        return newChromosomes;
    }

    static Chromosome mutate(Chromosome hrom,double rate){
        Random rand = new Random();
        if(rand.nextDouble() > rate){
            hrom.setCoef1(hrom.getCoef1()+rand.nextGaussian()*rand.nextInt(5000));
            hrom.setCoef2(hrom.getCoef2()+rand.nextGaussian()*rand.nextInt(5000));
            hrom.setCoef3(hrom.getCoef3()+rand.nextGaussian()*rand.nextInt(5000));
            hrom.setCoef4(hrom.getCoef4()+rand.nextGaussian()*rand.nextInt(5000));
            hrom.setClosestPellet(hrom.getClosestPellet()+rand.nextGaussian()*rand.nextInt(5000));
        }
        return hrom;
    }

}
