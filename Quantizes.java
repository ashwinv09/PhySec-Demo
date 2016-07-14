import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;

/**
 * Created by ashwinv on 20.06.16 at 16:00.
 */
public interface Quantizes {
    ArrayList<Byte> quantize(ArrayList<Integer> block, double alpha);
}

class JanaQuantization implements Quantizes {

    class Threshold {
        private int upper_threshold, lower_threshold;

        public Threshold(int upper_threshold, int lower_threshold) {
            this.upper_threshold = upper_threshold;
            this.lower_threshold = lower_threshold;
        }

        public int getUpper_threshold() {
            return upper_threshold;
        }

        public int getLower_threshold() {
            return lower_threshold;
        }
    }

    private Threshold calcThreshold(ArrayList<Integer> block, double alpha) {
        double[] arrBlock = block.stream().mapToDouble(i -> i).toArray();
        double mean = StatUtils.mean(arrBlock);
        double std = FastMath.sqrt(StatUtils.variance(arrBlock));

        int uT = (int)(mean + alpha * std);
        int lT = (int)(mean - alpha * std);

        return new Threshold(uT, lT);
    }

    @Override
    public ArrayList<Byte> quantize(ArrayList<Integer> block, double alpha) {
        Threshold threshold = calcThreshold(block, alpha);
//        ArrayList<Integer> index_list = new ArrayList<>();
        ArrayList<Byte> bitBlock = new ArrayList<>();
//        ArrayList<Integer> index_list = block.stream().filter(i -> (i <= threshold.getUpper_threshold()) && (i >= threshold.getLower_threshold())).collect(Collectors.toCollection(ArrayList::new));

        for (int i=0; i<block.size(); i++) {
            /*if ((block.get(i) > threshold.getUpper_threshold()) || (block.get(i) < threshold.getLower_threshold()))
                bitBlock.add();
            //index_list.add(i);*/
            if (block.get(i) > threshold.getUpper_threshold())
                bitBlock.add((byte)1);
            else if (block.get(i) < threshold.getLower_threshold())
                bitBlock.add((byte)0);
        }

        return bitBlock;
    }
}
