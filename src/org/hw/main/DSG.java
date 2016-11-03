package org.hw.main;

import org.hw.struct.GraphPoint;
import org.hw.struct.TwoDim;
import org.hw.util.ReadData;

import java.util.ArrayList;

/**
 * Created by gzf on 2016/10/26.
 * 生成有向图
 */
public class DSG {
    public ArrayList<ArrayList<GraphPoint<TwoDim>>> sortByLayer(ArrayList<TwoDim> ps, int k){
        ArrayList<ArrayList<GraphPoint<TwoDim>>> layers = new ArrayList<>();
        for (int i = 0; i < k; i++) layers.add(new ArrayList<>());
        for (TwoDim p : ps) if (p.layer <= k) layers.get(p.layer-1).add(new GraphPoint<>(p));
        return layers;
    }
    public ArrayList<ArrayList<GraphPoint<TwoDim>>> getKDSG(ArrayList<ArrayList<GraphPoint<TwoDim>>> layers, int k){
        for (int i = 0; i < layers.size(); i++) {
            for (int j = 0; j < layers.get(i).size(); j++) {
                GraphPoint<TwoDim> p = layers.get(i).get(j);
                // 可以将线性搜索改成二分查找
                for (int m = 0; m < i; m++) {
                    for (GraphPoint<TwoDim> gp : layers.get(m)) {
                        if (isDominate(gp.p, p.p)) p.parents.add(gp);
                    }
                }
                for (int m = i+1; m < layers.size(); m++) {
                    for (GraphPoint<TwoDim> gp : layers.get(m)) {
                        if (isDominate(p.p, gp.p)) p.children.add(gp);
                    }
                }
            }
        }
        return layers;
    }


    private static boolean isDominate(TwoDim p1, TwoDim p2){
        return (p1.y - p2.y <= 0) && (p1.x - p2.x <= 0);
    }

    public static void main(String[] args) {
        Layer2D layer = new Layer2D();
        ArrayList<TwoDim> ps = ReadData.read2DData("hotel.txt");
        layer.getKSkylineLayers(ps,ps.size());

        int k = 4;

        DSG dsg = new DSG();
        ArrayList<ArrayList<GraphPoint<TwoDim>>> layers = dsg.sortByLayer(ps, k);
//        for (ArrayList<GraphPoint<TwoDim>> twoDims : layers) {
//            for (GraphPoint<TwoDim> p : twoDims) {
//                System.out.println(p.p.layer+ " " + p.p.x + " " + p.p.y);
//            }
//        }
        dsg.getKDSG(layers, k);
        for (ArrayList<GraphPoint<TwoDim>> twoDims : layers) {
            for (GraphPoint<TwoDim> p : twoDims) {
                System.out.println(p.p.layer+ " " + p.p.x + " " + p.p.y);
                for (GraphPoint<TwoDim> child : p.children) {
                    System.out.print("(" + child.p.x + " " + child.p.y + " " + child.p.layer + ") ");
                }
                System.out.println();
            }
        }
    }
}
