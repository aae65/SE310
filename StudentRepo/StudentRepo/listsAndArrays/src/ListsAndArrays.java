import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ListsAndArrays {
    /*
        IMPORTANT: As a general statement: You should *always* favor ArrayList in Java.
        LinkedLists are less efficient under almost all use cases, even when big-o may
        favor a LinkedList due to the way the underlying memory works. A plain array
        offers little to no performance benefits over an ArrayList outside of very
        specific cases that will not be touched on in this course.
     */
    public static void main(String[] args){
        // A Java Array is similar to what you see in c/c++, a fixed size data
        // structure that stores elements of the same type in a contiguous memory block
        // Below we declare and instantiate an array that can hold 5 strings. A basic
        // array is not resizable in Java. Elements can be zeroed out but they cannot
        // be fully removed from an array
        String[] stringsArray = new String[5];

        // Set elements in the Array
        stringsArray[0] = "String 0";
        stringsArray[1] = "String 1";
        stringsArray[2] = "String 2";
        stringsArray[3] = "String 3";
        stringsArray[4] = "String 4";

        // Print the size of the array we just added elements to
        // NOTE: the size of an array is an attribute of the array, defined as length
        System.out.println("stringsArray size: " + stringsArray.length);

        // An array can also be created with values to store in the array, the size of
        // the array will be deduced by the values to be stored. This array is also not
        // resizable after creation.
        String[] preDefinedArray = new String[]{"one", "two", "three"};

        // Print the size of the pre-defined array
        System.out.println("preDefinedArray size: " + preDefinedArray.length);

        // List is an interface that ArrayList implements. It defines certain functions
        // which take specific input and give specific output that ArrayList, and all
        // other classes that implement List, need to provide.
        List<String> stringsArrayList = new ArrayList<>();

        // Print the ArrayList initial size
        // NOTE that size() is a function on a List, not an attribute like on an array
        System.out.println("stringsArrayList initial size: " + stringsArrayList.size());

        // Add elements to the ArrayList
        stringsArrayList.add("String 1");
        stringsArrayList.add("String 2");
        stringsArrayList.add("String 3");

        // Print the stringsArrayList size after adding 3 elements
        System.out.println("stringsArrayList new size: " + stringsArrayList.size());

        // We can fully remove elements from Lists in Java
        stringsArrayList.remove(0); // Remove the element at index 0
        System.out.println("stringsArrayList size after remove: " + stringsArrayList.size());

        // LinkedList also implements List, providing the same core functions. The
        // difference between ArrayList and LinkedList largely revolves around big-o
        // and which types of operations run more quickly. Looking up a generic array
        // vs linked list comparison can be done on your own time.
        List<String> stringsLinkedList = new LinkedList<>();
        System.out.println("Linked list initial size: " + stringsLinkedList.size());

        // Because ArrayList and LinkedList are both of type List we can create add all
        // elements from an ArrayList to a LinkedList. We can also pass a List to
        // another List's constructor to create the object
        stringsLinkedList.addAll(stringsArrayList);
        System.out.println("Linked list size after adding ArrayList: " + stringsLinkedList.size());

        // Because we declared the ArrayList and the LinkedList as a generic List we
        // can pass them around and treat them as if they are the same type. Here we'll
        // pass both the ArrayList and LinkedList to a function that converts them to a
        // basic array using existing library functions
        String[] fromArrayList = convertListToArray(stringsArrayList);
        String[] fromLinkedList = convertListToArray(stringsLinkedList);

        // Now we'll convert an array back to a generic list
        List<String> returnedList = convertArrayToList(fromArrayList);

        // Now show the elements in the original stringsArrayList, the array converted
        // from the original ArrayList, and finally the returned list that came from
        // the array. All objects should hold the same elements
        System.out.println("Elements in stringsArrayList:" );
        for(String s : stringsArrayList)
            System.out.println(s);

        System.out.println("Elements in array from stringsArrayList:");
        for(String s : fromArrayList)
            System.out.println(s);

        System.out.println("Elements in returnedList, from the array created from " +
                "stringsArrayList");
        for(String s : returnedList)
            System.out.println(s);

    }

    // This function takes in any type of List and converts it to a Java Array of Strings
    public static String[] convertListToArray(List<String> inputStringList){
        // NOTE: The toArray function needs to know what type of object is stored in
        // the List and therefore takes in an array of the correct type as a parameter
        String[] arr = inputStringList.toArray(new String[inputStringList.size()]);
        return arr;
    }

    // This function takes in an array and returns a generic List
    public static List<String> convertArrayToList(String[] array){
        // NOTE:The Array.asList function probably returns an ArrayList but it's not
        // defined so each implementation can make that choice, therefore the return
        // type needs to be specified as a generic List. This is one reason it's
        // important to use the most basic class in the inheritance hierarchy as
        // possible in Java
        List<String> list = Arrays.asList(array);
        return list;
    }
}





