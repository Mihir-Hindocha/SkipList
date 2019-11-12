This is a readme file containing instructions for executing code for Long Project 2 - Implementation of SkipList

Authors:
	1. Mihir Hindocha   - mxh170027
	2. Nihal Abdulla PT - nxp171730
	3. Amal Mohan       - axm179030

Steps for running code from the cmd prompt
1.Go to the directory containing the package nxp171730 is present
2.Copy paste the SkipListDriver.java and Timer.java to the package
2.Compile the SkipListDriver.java by executing the following commands
		>javac nxp171730/SkipListDriver.java
		>javac nxp171730/Timer.java
		>javac -Xlint nxp171730/SkipList.java
		
3.To execute the main(driver) code
		>java nxp171730/SkipListDriver {path to lp2-inputs}/lp2-inputs/lp2-in{fileNumber}.txt

IMPLEMENTED
===========
1.add(x)
2.ceiling(x)
3.contains(x)
4.first()
5.floor(x)
6.get(n)
7.getLinear(n)
8.getLog(n)
9.isEmpty()
10.iterator()
11.last()
12.rebuild()
13.remove(x)
14.size()

OUTPUT for the provided input files
==========================
1.lp2-in01.txt
	36
	Time: 9 msec.
	Memory: 5 MB / 245 MB.

2.lp2-in02.txt
	721
	Time: 28 msec.
	Memory: 6 MB / 245 MB.

3.lp2-in03.txt
	70918
	Time: 381 msec.
	Memory: 47 MB / 245 MB.

4.lp2-in11.txt
	163
	Time: 9 msec.
	Memory: 5 MB / 245 MB.

5.lp2-in12.txt
	1013
	Time: 12 msec.
	Memory: 5 MB / 245 MB.

6.lp2-in13.txt
	4002
	Time: 16 msec.
	Memory: 5 MB / 245 MB.

7.lp2-in14.txt
	445138
	Time: 4960 msec.
	Memory: 43 MB / 245 MB.


DRIVER CODE
===========
public static void main(String[] args) {
        SkipList<Integer> sl = new SkipList<>();
        System.out.println("IsEmpty: " + sl.isEmpty());
        System.out.println("Skiplist Size: " + sl.size());
        for(int i=1; i<=136; i++)
            sl.add(i);
        System.out.println("Added elements 1 to 128 to list (indexed 0 to 99)");
        System.out.println("IsEmpty: " + sl.isEmpty());
        System.out.println("Skiplist Size: " + sl.size());
        System.out.println("Skiplist Maxlevel: " + sl.maxLevel);
        System.out.println("First element at Maxlevel: " + sl.head.next[sl.maxLevel-1].getElement());
        System.out.println("Skiplist elements and length of next array for each element");
        sl.printSortedList();
        System.out.println();
        System.out.println("First Element: " + sl.first());
        System.out.println("Element at position 2: " + sl.getLinear(2));
        System.out.println("Last Element: " + sl.last());
        System.out.println("Floor(101): " + sl.floor(101));
        System.out.println("Ceiling(0): " + sl.ceiling(0));
        System.out.println("Removed elements {4, 23, 42, 34, 54, 68, 75, 79} using iterator remove method");
        Iterator<Integer> it = sl.iterator();
        while (it.hasNext())
        {
            Integer s=it.next();
            if(s==4||s==23||s==42||s==34||s==54||s==68||s==75||s==79){
                it.remove();
            }
        }
        System.out.println("Floor(4): " + sl.floor(4));
        System.out.println("Ceiling(4): " + sl.ceiling(4));
        System.out.println("Skiplist Size: " + sl.size());
        System.out.println("Skiplist Maxlevel: " + sl.maxLevel);
        sl.printSortedList();
        System.out.println();
        System.out.println("Skiplist rebuilt");
        sl.rebuild();
        sl.printSortedList();
        System.out.println();
        System.out.println("Skiplist Maxlevel: " + sl.maxLevel);
        System.out.println("First element at Maxlevel: " + sl.head.next[sl.maxLevel-1].getElement());
    }

OUTPUT
======
IsEmpty: true
Skiplist Size: 0
Added elements 1 to 128 to list (indexed 0 to 99)
IsEmpty: false
Skiplist Size: 136
Skiplist Maxlevel: 10
First element at Maxlevel: 67
Skiplist elements and length of next array for each element
1 4->2 1->3 3->4 1->5 1->6 4->7 1->8 1->9 2->10 1->11 2->12 2->13 2->14 3->15 2->16 4->17 2->18 4->19 2->20 1->21 1->22 2->23 1->24 3->25 2->26 1->27 1->28 4->29 1->30 2->31 1->32 1->33 4->34 1->35 3->36 2->37 2->38 1->39 1->40 1->41 2->42 2->43 3->44 2->45 1->46 2->47 1->48 1->49 2->50 1->51 1->52 4->53 3->54 4->55 3->56 1->57 2->58 6->59 1->60 2->61 1->62 1->63 2->64 1->65 1->66 2->67 10->68 2->69 2->70 1->71 2->72 1->73 1->74 1->75 1->76 1->77 5->78 4->79 2->80 1->81 1->82 5->83 1->84 1->85 4->86 1->87 1->88 2->89 1->90 2->91 3->92 3->93 1->94 1->95 2->96 8->97 1->98 1->99 1->100 1->101 3->102 1->103 3->104 1->105 4->106 1->107 1->108 1->109 3->110 3->111 2->112 1->113 1->114 1->115 2->116 6->117 2->118 1->119 2->120 1->121 2->122 1->123 5->124 1->125 1->126 5->127 1->128 7->129 2->130 2->131 1->132 1->133 1->134 6->135 1->136 1->
First Element: 1
Element at position 2: 3
Last Element: 136
Floor(101): 101
Ceiling(0): 1
Removed elements {4, 23, 42, 34, 54, 68, 75, 79} using iterator remove method
Floor(4): 3
Ceiling(4): 5
Skiplist Size: 128
Skiplist Maxlevel: 10
1 4->2 1->3 3->5 1->6 4->7 1->8 1->9 2->10 1->11 2->12 2->13 2->14 3->15 2->16 4->17 2->18 4->19 2->20 1->21 1->22 2->24 3->25 2->26 1->27 1->28 4->29 1->30 2->31 1->32 1->33 4->35 3->36 2->37 2->38 1->39 1->40 1->41 2->43 3->44 2->45 1->46 2->47 1->48 1->49 2->50 1->51 1->52 4->53 3->55 3->56 1->57 2->58 6->59 1->60 2->61 1->62 1->63 2->64 1->65 1->66 2->67 10->69 2->70 1->71 2->72 1->73 1->74 1->76 1->77 5->78 4->80 1->81 1->82 5->83 1->84 1->85 4->86 1->87 1->88 2->89 1->90 2->91 3->92 3->93 1->94 1->95 2->96 8->97 1->98 1->99 1->100 1->101 3->102 1->103 3->104 1->105 4->106 1->107 1->108 1->109 3->110 3->111 2->112 1->113 1->114 1->115 2->116 6->117 2->118 1->119 2->120 1->121 2->122 1->123 5->124 1->125 1->126 5->127 1->128 7->129 2->130 2->131 1->132 1->133 1->134 6->135 1->136 1->
Skiplist rebuilt
Levels: 8
1 1->2 2->3 1->5 3->6 1->7 2->8 1->9 4->10 1->11 2->12 1->13 3->14 1->15 2->16 1->17 5->18 1->19 2->20 1->21 3->22 1->24 2->25 1->26 4->27 1->28 2->29 1->30 3->31 1->32 2->33 1->35 6->36 1->37 2->38 1->39 3->40 1->41 2->43 1->44 4->45 1->46 2->47 1->48 3->49 1->50 2->51 1->52 5->53 1->55 2->56 1->57 3->58 1->59 2->60 1->61 4->62 1->63 2->64 1->65 3->66 1->67 2->69 1->70 7->71 1->72 2->73 1->74 3->76 1->77 2->78 1->80 4->81 1->82 2->83 1->84 3->85 1->86 2->87 1->88 5->89 1->90 2->91 1->92 3->93 1->94 2->95 1->96 4->97 1->98 2->99 1->100 3->101 1->102 2->103 1->104 6->105 1->106 2->107 1->108 3->109 1->110 2->111 1->112 4->113 1->114 2->115 1->116 3->117 1->118 2->119 1->120 5->121 1->122 2->123 1->124 3->125 1->126 2->127 1->128 4->129 1->130 2->131 1->132 3->133 1->134 2->135 1->136 8->
Skiplist Maxlevel: 8
First element at Maxlevel: 136
