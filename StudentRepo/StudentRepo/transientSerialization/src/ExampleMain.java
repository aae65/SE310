import java.util.List;

public class ExampleMain {
    public static void main(String[] args){
        TransientWhenSerializing tws = new TransientWhenSerializing("TestFileLines");

        List<String> lines = tws.readFileLineByLine();
        System.out.println("Original Lines:");
        for(String s : lines)
            System.out.println(s);

        String savePath = TransientWhenSerializing.serialize(tws);
        TransientWhenSerializing deserialized =
                TransientWhenSerializing.deserialize(savePath);

        List<String> deserializedLines = deserialized.readFileLineByLine();
        System.out.println("Deserialized Lines:");
        for(String s : deserializedLines)
            System.out.println(s);
    }
}
