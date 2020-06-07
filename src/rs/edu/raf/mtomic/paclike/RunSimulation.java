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
//        long startTime = System.nanoTime();
//        ArrayList<Chromosome> population = new ArrayList<Chromosome>();
//        Random random = new Random();
//
//        for(int i=0; i<250;i++){
//            population.add(new Chromosome(random.nextDouble()*5000,random.nextDouble()*5000,random.nextDouble()*5000,random.nextDouble()*5000,random.nextDouble()*5000));
//        }
//
//        Chromosome best_ever_chrom = null;
//        int best_ever_total_points = 0;
//
//        int t=0;
//        int max_iter = 5;
//
//        while(t < max_iter){
//            for(int i=0; i<50;i++){
//                population.add(new Chromosome(random.nextGaussian()*random.nextDouble()*5000,random.nextGaussian()*random.nextDouble()*5000,random.nextGaussian()*random.nextDouble()*5000,random.nextGaussian()*random.nextDouble()*5000,random.nextGaussian()*random.nextDouble()*5000));
//            }
//            while(population.size() < 600){
//                Chromosome h1 = tournament(population,10);
//                Chromosome h2 = tournament(population,10);
//                ArrayList<Chromosome> crossoverChromosomes = crossover(h1,h2);
//                population.add(mutate(crossoverChromosomes.get(0),0.4));
//                population.add(mutate(crossoverChromosomes.get(1),0.4));
//            }
//            SortedMap<Integer,ArrayList<Chromosome>> sm = new TreeMap<Integer, ArrayList<Chromosome>>();
//            for(Chromosome c:population){
//                PacLike pacLike = new PacLike(new PlayerOne(null,c));
//                int points = -1;
//                try {
//                    pacLike.join(200);
//                    points = pacLike.getTotalPoints();
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                if(sm.containsKey(points)){
//                    sm.get(points).add(c);
//                }else{
//                    ArrayList a = new ArrayList<Chromosome>();
//                    a.add(c);
//                    sm.put(points,a);
//                }
//            }
//            if(sm.lastKey() > best_ever_total_points){
//                best_ever_total_points = sm.lastKey();
//                best_ever_chrom = sm.get(sm.lastKey()).get(0);
//            }
//            population.clear();
//            while(population.size() < 250){
//                for(Chromosome chr : (ArrayList<Chromosome>)sm.get(sm.lastKey())){
//                    population.add(new Chromosome(chr.getCoef1(),chr.getCoef2(),chr.getCoef3(),chr.getCoef4(),chr.getClosestPellet()));
//                }
//                sm.remove(sm.lastKey());
//            }
//            if(population.size() > 250){
//                population.subList(250,population.size()-1).clear();
//            }
//            t++;
//            System.out.println(best_ever_chrom.toString()+best_ever_total_points);
//            if(best_ever_total_points==244)break;
//        }
//
//        System.out.println("Best chrom:"+best_ever_chrom.toString()+best_ever_total_points);
//        long stopTime = System.nanoTime();
//        long convert = TimeUnit.SECONDS.convert(stopTime - startTime, TimeUnit.NANOSECONDS);
//        System.out.println(convert + " seconds");


        //Chromosome{coef1=-373.15395574447797, coef2=853.7562360652328, coef3=585.9539017878902, coef4=-326.71653659364046, closestPellet=356.3019048955662}143
        //Chromosome{coef1=443.18586876105525, coef2=-139.98841940847637, coef3=57.56170463460822, coef4=71.69481109959915, closestPellet=145.92591544442445}158
        //Chromosome{coef1=-410.5854687450856, coef2=483.4398347378499, coef3=441.9626766011047, coef4=-214.12811833806566, closestPellet=1000.0}171
        //Chromosome{coef1=-235.73197203694582, coef2=354.22628493857485, coef3=963.6507579562339, coef4=-453.0679315652816, closestPellet=1000.0}179
        //Chromosome{coef1=-124.87421986791924, coef2=870.9589101752979, coef3=253.62699454875903, coef4=-787.1563321723659, closestPellet=2000.0}190
        //Chromosome{coef1=2542.9893003445036, coef2=192.35297845504468, coef3=-407.8174513893016, coef4=398.29707380013485, closestPellet=878.9128538341108}218
        //Chromosome{coef1=4449.838827373452, coef2=67.13365445010284, coef3=77.07763904662667, coef4=-586.2948693309, closestPellet=1404.3303276854583}228
        //Chromosome{coef1=4932.023316559889, coef2=1975.450901856315, coef3=-4984.918768154561, coef4=2368.203780343232, closestPellet=4871.73399832017}231

//        // KRAJ NAJBOLJE RESENJE ->
//        //Chromosome{coef1=4004.644390730013, coef2=-55.14120393717613, coef3=-702.9602980503226, coef4=281.26908428888237, closestPellet=5000.0}243
//
        PacLike pacLike = new PacLike(new PlayerOne(null,new Chromosome(4004.644390730013,-55.14120393717613,-702.9602980503226,281.26908428888237,5000.0)));
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
            PacLike pacLike = new PacLike(new PlayerOne(null,ch));
            int points = -1;
            try {
                pacLike.join(200);
                points = pacLike.getTotalPoints();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(points > best_chrom_points){
                best_chrom_points = points;
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
