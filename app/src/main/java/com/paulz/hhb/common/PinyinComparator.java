package com.paulz.hhb.common;//package com.paulz.hhb.common;
//
//import java.util.Comparator;
//
//import com.paulz.hhb.model.BrandInfo;
//
///**
// * 国际大牌
// * 
// * @author jjj
// * 
// * @time 2015-12-28
// */
//public class PinyinComparator implements Comparator<BrandInfo> {
//
//	public int compare(BrandInfo o1, BrandInfo o2) {
//		if (o1.getLetter().equals("@") || o2.getLetter().equals("#")) {
//			return -1;
//		} else if (o1.getLetter().equals("#") || o2.getLetter().equals("@")) {
//			return 1;
//		} else {
//			return o1.getLetter().compareTo(o2.getLetter());
//		}
//	}
//
//}
