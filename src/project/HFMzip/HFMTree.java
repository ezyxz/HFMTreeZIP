package project.HFMzip;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HFMTree {
	HFMNode root;
	class HFMNode{
		representation oRepresentation;
		HFMNode left;
		HFMNode right; 
		public HFMNode(representation oRepresentation) {
			this.oRepresentation = oRepresentation;
		}
		@Override
		public String toString() {
			return oRepresentation.count+"";
		}
	}
	
	public HFMNode generateHFMTree(List<representation> list_rep) {
		
		Collections.sort(list_rep, new Comparator<representation>() {
	        @Override
	        public int compare(representation o1, representation o2) {
	        return o1.count - o2.count;
	        }
	    });
		List<HFMNode> list_node = new ArrayList<HFMNode>();
		while(!list_rep.isEmpty()) {
			list_node.add(new HFMNode(list_rep.remove(0)));
		}
		while(list_node.size()>=2) {
			HFMNode left = list_node.remove(0);
			HFMNode right = list_node.remove(0);
			representation parent = new representation(-1, left.oRepresentation.count + right.oRepresentation.count);
			root = new HFMNode(parent);
			root.left = left;
			root.right = right;
			list_node.add(root);

			Collections.sort(list_node, new Comparator<HFMNode>() {
				@Override
				public int compare(HFMNode o1, HFMNode o2) {
					return o1.oRepresentation.count - o2.oRepresentation.count;
				}
			});
			
		}
				
		return root;
		
	}
	

    
    public HFMNode initTestRoot() {
    	HFMNode root = new HFMNode(new representation(-1,21));
    	root.left = new HFMNode(new representation(-1,12));
    	root.right = new HFMNode(new representation(-1,9));
    	root.left.left = new HFMNode(new representation(-1,6));
    	root.left.right = new HFMNode(new representation(-1,6));
    	root.right.left = new HFMNode(new representation(-1,5));
    	root.right.right = new HFMNode(new representation(-1,4));
    	root.left.left.left = new HFMNode(new representation(-1,3));
    	root.left.left.right = new HFMNode(new representation(-1,3));
    	root.left.left.left.left = new HFMNode(new representation(-1,2));
    	root.left.left.left.right = new HFMNode(new representation(-1,1));
		return root;
    	
    }


}
