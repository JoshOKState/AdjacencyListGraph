import java.io.*;
import java.util.*;

public class Assignment05_Inman_Joshua_Main {
    private static class Student {
        final String id, studentsFirstName, studentsLastName, college, department, email;
        int friendCount;
        public Student(String id, String firstName, String lastName, String college, String department, String email, int friendCount) {
            this.id = id;
            studentsFirstName = firstName;
            studentsLastName = lastName;
            this.college = college;
            this.department = department;
            this.email = email;
            this.friendCount = friendCount;
        }

        public Student(String id) {
            this.id = id;
            studentsFirstName = null;
            studentsLastName = null;
            college = null;
            department = null;
            email = null;
            friendCount = 0;
        }

        public Student(String id, String firstName) {
            this.id = id;
            studentsFirstName = firstName;
            studentsLastName = null;
            college = null;
            department = null;
            email = null;
            friendCount = 0;
        }

        public boolean isEqual(Student otherStudent) {
            return(this.studentsFirstName.equals(otherStudent.getStudentsFirstName()) || this.id.equals(otherStudent.getId()));
        }

        public String getId() { return id; }
        public String getStudentsFirstName() { return studentsFirstName; }

        public String toString() { return studentsFirstName + " " + studentsLastName + ": " + id; }
        public String getCollege() { return college; }
    }

    private static class Friendship {
        int weight;
        public Friendship() { weight = 1; } // graph is unweighted i.e. all edges are equal weight

        public int getElement() { return weight; }
    }

    /** Prints menu of available options for user */
    public static void printMenu() {
        System.out.print("\n1. Remove friendship\n2. Delete Account\n3. Count friends\n4. Friends Circle\n5. Closeness centrality\n6. Find Connectors\n7. Exit\n\nPlease make a selection (enter 1-7): ");
    }

    /** Gets user input and returns as int if possible (-1 otherwise) */
    static int getUserSelection(Scanner scnr) throws InputMismatchException {
        printMenu();
        try {
            int selection = scnr.nextInt();
            scnr.nextLine();
            return selection;
        } catch (InputMismatchException E) {
            scnr.nextLine();
            return -1;
        }
    }

    /**
     * Parses file with given name and adds entries to the Graph
     * @param filename the name of the file to read
     * @param graph a graph to which vertices and edges may be added
     * @return true if file read correctly, false if exception occurs
     */
    static boolean getDataFromFile(String filename, AdjacencyListGraph<Student, Friendship> graph) {
        String filepath = "./" + filename;
        File file = new File(filepath);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            reader.readLine();  // Skip first line of input file (headers)
            // Insert all vertices
            while(reader.ready()) {
                String[] nextInput = reader.readLine().split("\t");
                String id = nextInput[0], firstName = nextInput[1], lastName = nextInput[2],
                        college = nextInput[3].replaceAll("\"", ""), department = nextInput[4], email = nextInput[5];
                int friendCount = Integer.parseInt(nextInput[6]);
                Student s = new Student(id, firstName, lastName, college, department, email, friendCount);
                graph.insertVertex(s);
            }
            // Go back to beginning of file to read existing friendships
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            reader.readLine();  // Skip first line of input file (headers)
            // Insert edges
            while(reader.ready()) {
                String[] nextInput = reader.readLine().split("\t");
                String id = nextInput[0], firstName = nextInput[1], lastName = nextInput[2],
                        college = nextInput[3], department = nextInput[4], email = nextInput[5];
                int friendCount = Integer.parseInt(nextInput[6]);
                Student s = new Student(id, firstName, lastName, college, department, email, friendCount);
                Iterable<Vertex<Student>> vertices = graph.vertices();
                Vertex currentVertex = null, newFriend = null;
                for(Vertex v : vertices) {
                    Student current = (Student) v.getElement();
                    if(current.isEqual(s)) {
                        currentVertex = v;
                        break;
                    }
                }
                for(int i = 7; i < nextInput.length; ++i) {
                    Student friend = new Student(nextInput[i]);
                    for(Vertex v : vertices) {
                        Student current = (Student)v.getElement();
                        if(current.isEqual(friend)) {
                            newFriend = v;
                            break;
                        }
                    }
                    try {
                        graph.insertEdge(currentVertex, newFriend, new Friendship());
                    } catch(IllegalArgumentException e) {}
                }
            }
            System.out.println("Input file is read successfully..");
            printInfo(graph);
            return true;
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found.");
            return false;
        } catch (IOException e) {
            System.out.println("Error occurred while receiving the data.");
            return false;
        }
    }

    /**
     * Performs a breadth-first search of a given Graph and returns a Forest of vertices reached during search that match given college name
     * Used to generate friend circles in a given college
     * @param g the graph to search
     * @param s the Vertex at which to begin the search
     * @param known a set of Vertices which have already been traversed during search
     * @param forest a forest to update with matching Vertices
     * @param collegeName the name of the college matching Vertices belong to
     */
    // Breadth-first search of graph
    public static void collegeBFS(AdjacencyListGraph<Student,Friendship> g, Vertex<Student> s, Set<Vertex<Student>> known, Map<Vertex<Student>,
            Edge<Friendship>> forest, String collegeName) {
        PositionalList<Vertex<Student>> level = new LinkedPositionalList<>();
        forest.put(s, null);
        known.add(s);
        level.addLast(s);
        while(!level.isEmpty()) {
            PositionalList<Vertex<Student>> nextLevel = new LinkedPositionalList<>();
            for(Vertex<Student> u: level) {
                for(Edge<Friendship> e : g.outgoingEdgeList(u)) {
                    Vertex<Student> v = g.opposite(u, e);
                    Student vStudent = v.getElement();
                    String vCollege = vStudent.getCollege();
                    if(!known.contains(v)) {
                        known.add(v);
                        if(collegeName.equals(vCollege)) forest.put(v, e);
                        nextLevel.addLast(v);
                    }
                }
                level = nextLevel;
            }
        }
        int i = 1;
        for (Entry<Vertex<Student>, Edge<Friendship>> entry : forest.entrySet()) {
            System.out.print(entry.getKey().getElement().getStudentsFirstName());
            if(!(i++ == forest.size())) System.out.print(" - ");
            else System.out.println();
        }
    }

    /**
     * Finds the shortest path lengths between starting Vertex and other vertices in graph
     * Dijkstra's Algorithm
     * @param g the graph to be searched
     * @param src  the root of the search
     * @return a Map of shortest lengths from src to all other reachable Vertices
     */
    // Dijkstra's algorithm
    public static <Student> Map<Vertex<Student>, Integer> shortestPathLengths(AdjacencyListGraph<Student, Friendship> g, Vertex<Student> src) {
        // d.get(v) is upper bound on distance from src to v
        Map<Vertex<Student>, Integer> d = new ProbeHashMap<>();
        // map reachable v to its d value
        Map<Vertex<Student>, Integer> cloud = new ProbeHashMap<>();
        // pq will have vertices as elements, with d.get(v) as key
        AdaptablePriorityQueue<Integer, Vertex<Student>> pq;
        pq = new HeapAdaptablePriorityQueue<>();
        Map<Vertex<Student>, Entry<Integer, Vertex<Student>>> pqTokens;
        pqTokens = new ProbeHashMap<>();

        for (Vertex<Student> v : g.vertices()) {
            if(v == src)
                d.put(v,0);
            else
                d.put(v, Integer.MAX_VALUE);
            pqTokens.put(v, pq.insert(d.get(v), v));
        }

        while(!pq.isEmpty()) {
            Entry<Integer, Vertex<Student>> entry = pq.removeMin();
            int key = entry.getKey();
            Vertex<Student> u = entry.getValue();
            cloud.put(u, key);
            pqTokens.remove(u);
            for (Edge<Friendship> e : g.outgoingEdgeList(u)) {
                Vertex<Student> v = g.opposite(u, e);
                if (cloud.get(v) == null) {
                    if (d.get(u) + e.getElement().getElement() < d.get(v)) {
                        d.put(v, d.get(u) + e.getElement().getElement());
                        pq.replaceKey(pqTokens.get(v), d.get(v));
                    }
                }
            }
        }
        return cloud;
    }

    /**
     * Performs a depth-first search on a given graph
     * @param g the graph to be searched
     * @param u the Vertex at which to begin the search
     * @param known a map of previously visited vertices
     * @param forest a map to update with vertices in the order they are traversed
     */
    public static <Student, Friendship> void DFS(AdjacencyListGraph<Student,Friendship> g, Vertex<Student> u,
                                                 Set<Vertex<Student>> known, Map<Vertex<Student>, Edge<Friendship>> forest) {
        known.add(u);
        for(Edge<Friendship> e : g.outgoingEdgeList(u)) {
            Vertex<Student> v = g.opposite(u,e);
            if(!known.contains(v)) {
                forest.put(v,e);
                DFS(g,v,known,forest);
            }
        }
    }

    /**
     * Constructs a path from given vertex u to given vertex v
     * @param g the graph to be searched
     * @param u the starting vertex
     * @param v the vertex to be reached
     * @param forest A map of vertices reached and edges travelled
     * @return a list of edges travelled
     */
    public static <Student,Friendship> PositionalList<Edge<Friendship>> constructPath(Graph<Student,Friendship> g, Vertex<Student> u,
                                                                                      Vertex<Student> v, Map<Vertex<Student>, Edge<Friendship>> forest) {
        PositionalList<Edge<Friendship>> path = new LinkedPositionalList<>();
        if(forest.get(v) != null) {
            Vertex<Student> walk = v;
            while (walk != u) {
                Edge<Friendship> edge = forest.get(walk);
                path.addFirst(edge);
                walk = g.opposite(walk, edge);
            }
        }
        return path;
    }

    /**
     * Performs depth-first search on unconnected graph
     * @param g the graph to be searched
     * @return a Map representing a forest of Vertices and Edges traversed
     */
    public static <Student,Friendship> Map<Vertex<Student>, Edge<Friendship>> DFSComplete(AdjacencyListGraph<Student,Friendship> g) {
        Set<Vertex<Student>> known = new HashSet<>();
        Map<Vertex<Student>, Edge<Friendship>> forest = new ProbeHashMap<>();
        for(Vertex<Student> u : g.vertices())
            if(!known.contains(u)) DFS(g, u, known, forest);
        return forest;
    }

    /** prints the number of vertices and edges in the graph to the console */
    public static void printInfo(AdjacencyListGraph<Student,Friendship> graph) {
        System.out.println("Total number of vertices in the graph: " + graph.numVertices());
        System.out.println("Total number of edges in the graph: " + graph.numEdges());
    }

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        AdjacencyListGraph<Student, Friendship> graph = new AdjacencyListGraph(false);
        boolean fileFound = false;
        while(!fileFound) {
            System.out.print("Please enter the file's name: ");
            fileFound = getDataFromFile(scnr.nextLine(), graph);
        }
        int selection = getUserSelection(scnr);
        while(selection != 7) {     // Continue until user enters 7 to exit
            Iterable<Vertex<Student>> vertices = graph.vertices();
            boolean found = false;
            switch (selection) {
                case 1:
                    // Remove friendship
                    System.out.print("Please enter the first name of the first student: ");
                    Student s1 = new Student(null, scnr.nextLine());
                    System.out.print("Please enter the first name of the second student: ");
                    Student s2 = new Student(null, scnr.nextLine());
                    Vertex<Student> v1 = null, v2 = null;
                    for (Vertex<Student> v : vertices) {
                        Student curr = v.getElement();
                        if (curr.isEqual(s1)) v1 = v;
                        if (curr.isEqual(s2)) v2 = v;
                        if (v1 != null && v2 != null) break;
                    }
                    if (v1 == null || v2 == null) {
                        System.out.println("Sorry..");
                        if (v1 == null) System.out.println(s1.getStudentsFirstName() + " not found!");
                        if (v2 == null) System.out.println(s2.getStudentsFirstName() + " not found!");
                    } else {
                        Edge<Friendship> toRemove = graph.getEdge(v1, v2);
                        try {
                            graph.removeEdge(toRemove);
                            System.out.println("The edge between the students " + s1.getStudentsFirstName() + " and " +
                                    s2.getStudentsFirstName() + " has been successfully removed..");
                            printInfo(graph);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Sorry.. There is no edge between the vertices " + s1.getStudentsFirstName() + " and " +
                                    s2.getStudentsFirstName() + ".");
                        }
                        //graph.nullifyEdge(toRemove);
                    }
                    break;
                case 2:
                    // Delete Account
                    System.out.print("Please enter the first name of the student to remove: ");
                    Student s = new Student(null, scnr.nextLine());
                    for (Vertex v : vertices) {
                        Student current = (Student) v.getElement();
                        if (current.isEqual(s)) {
                            found = true;
                            graph.removeVertex(v);
                            System.out.println("The student " + s.getStudentsFirstName() + " has been successfully removed.");
                            printInfo(graph);
                            break;
                        }
                    }
                    if (!found) System.out.println("Sorry..\n" + s.getStudentsFirstName() + " not found!");
                    break;
                case 3:
                    // Count friends
                    System.out.print("Please enter the name of the student: ");
                    Student lonely = new Student(null, scnr.nextLine());
                    for (Vertex v : vertices) {
                        Student current = (Student) v.getElement();
                        if (current.isEqual(lonely)) {
                            found = true;
                            Iterable<Edge<Friendship>> friends = graph.friendsList(v);
                            int count = 0;
                            for (Edge<Friendship> friendship : friends) {
                                if (friendship.getElement() != null) count += 1;
                            }
                            System.out.println("Friend count for " + current.getStudentsFirstName() + ": " + count);
                            System.out.println("Friends of " + current.getStudentsFirstName() + " are:");
                            for (Edge<Friendship> friendship : friends) {
                                if (friendship.getElement() != null) {
                                    Vertex<Student> friend = graph.opposite(v, friendship);
                                    System.out.println(friend.getElement().getStudentsFirstName());
                                }
                            }
                            break;
                        }
                    }
                    if (!found) System.out.println("Sorry..\n" + lonely.getStudentsFirstName() + " not found!");
                    break;
                case 4:
                    // Friend circle via BFS
                    System.out.print("Which college would you like to search? ");
                    String collegeName = scnr.nextLine().replaceAll("\"", "");
                    Set known = new HashSet();
                    System.out.println("Following are the friend circles in " + collegeName);
                    for (Vertex v : vertices) {
                        Map forest = new ProbeHashMap();
                        Student current = (Student) v.getElement();
                        if(current.getCollege().equals(collegeName) && !(known.contains(v)))
                            collegeBFS(graph, v, known, forest, collegeName);
                    }
                    break;
                case 5:
                    // Closeness centrality ?? la Dijkstra's Algorithm
                    System.out.print("Please enter the student's name: ");
                    Student howClose = new Student(null, scnr.nextLine());
                    for (Vertex v : vertices) {
                        Student current = (Student) v.getElement();
                        if (current.isEqual(howClose)) {
                            found = true;
                            Map<Vertex<Student>, Integer> lengths = shortestPathLengths(graph, v);
                            Iterable<Entry<Vertex<Student>, Integer>> entries = lengths.entrySet();
                            double sum = 0;
                            for (Entry<Vertex<Student>, Integer> entry : entries) {
                                if (entry.getValue() > 0 && entry.getValue() < 1000)
                                    sum += 1.0 / entry.getValue();
                            }
                            System.out.println("The Closeness Centrality for " + howClose.getStudentsFirstName() + ": " + sum);
                            System.out.println("The Normalized Closeness Centrality for " + howClose.getStudentsFirstName() + ": " + sum / (graph.numVertices() - 1));
                            break;
                        }
                    }
                    if (!found) System.out.println("Sorry..\n" + howClose.getStudentsFirstName() + " not found!");
                    break;
                case 6:
                    // Display connectors in graph
                    Map<Vertex<Student>, Edge<Friendship>> spanningForest = DFSComplete(graph);
                    if(spanningForest.size() == 0)
                        System.out.println("There are no connectors in the graph.");
                    else {
                        System.out.println("The connectors in the graph are as follows:");
                        for (Entry<Vertex<Student>, Edge<Friendship>> kv : spanningForest.entrySet())
                            System.out.println(kv.getKey().getElement().getStudentsFirstName() + " from " + kv.getKey().getElement().getCollege());
                    }
                    break;
                default:
                    // Selection was outside proper range
                        System.out.println("\nYou didn't have a valid input. Please enter 1-6.\n");
                        break;
            }
            // Get another selection from user
            selection = getUserSelection(scnr);
        }
    }
}