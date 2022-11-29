import java.io.*;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
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
//            System.out.println("this.FirstName = " + this.studentsFirstName + ", otherStudent.firstName = " + otherStudent.getStudentsFirstName() + ": " + this.studentsFirstName.equals(otherStudent.getStudentsFirstName()));
//            System.out.println("this.id = " + this.id + ", otherStudent.id = " + otherStudent.getId() + ": " + this.id.equals(otherStudent.getId()));
            return(this.studentsFirstName.equals(otherStudent.getStudentsFirstName()) || this.id.equals(otherStudent.getId()));
        }

        public String getId() { return id; }
        public int getFriendCount() { return friendCount; }
        public String getStudentsFirstName() { return studentsFirstName; }

        public String toString() { return studentsFirstName + " " + studentsLastName + ": " + id; }
    }

    private static class Friendship {
        boolean areFriends;
        public Friendship() { areFriends = true; }
    }

    /** Prints menu of available options for user */
    public static void printMenu() {
        System.out.println("\n1. Remove friendship\n2. Delete Account\n" +
                "3. Count friends\n4. Friends Circle\n5. Closeness centrality" +
                "\n6. Exit\n");
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

    static void getDataFromFile(String filename, AdjacencyListGraph<Student, Friendship> graph) {
        String filepath = "./" + filename;
        File file = new File(filepath);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            reader.readLine();  // Skip first line of input file (headers)
            // Insert all vertices
            while(reader.ready()) {
                String[] nextInput = reader.readLine().split("\t");
                String id = nextInput[0], firstName = nextInput[1], lastName = nextInput[2],
                        college = nextInput[3], department = nextInput[4], email = nextInput[5];
                int friendCount = Integer.parseInt(nextInput[6]);
                Student s = new Student(id, firstName, lastName, college, department, email, friendCount);
                graph.insertVertex(s);
                //System.out.println(newVertex.toString());
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
                    //System.out.println(current.toString() + ", " + s.toString() + ": " + current.isEqual(s));
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
                    graph.insertEdge(currentVertex, newFriend, new Friendship());
                }
            }
            System.out.println("Input file is read successfully..");
            System.out.println("Total number of vertices in the graph: " + graph.numVertices());
            System.out.println("Total number of edges in the graph: " + graph.numEdges());
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Error occurred while receiving the data.");
        }
    }

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        AdjacencyListGraph<Student, Friendship> graph = new AdjacencyListGraph(false);
        System.out.print("Please enter the file's name: ");
        getDataFromFile(scnr.nextLine(), graph);
        int selection = getUserSelection(scnr);
        while(selection != 6) {     // Continue until user enter 6 to exit
            switch(selection) {
                case 1:
                    // Remove friendship
                    break;
                case 2:
                    // Delete Account
                    break;
                case 3:
                    // Count friends
                    break;
                case 4:
                    // Closeness centrality
                    break;
                case 5:
                    break;
                default:
                    // Selection was outside proper range
                    System.out.println("\nYou didn't have a valid input. Please enter 1-7.\n");
            }
            // Get another selection from user
            selection = getUserSelection(scnr);
        }
    }
}