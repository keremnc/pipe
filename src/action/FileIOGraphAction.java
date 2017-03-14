package action;

import action.result.ActionResult;
import io.PipelineContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class FileIOGraphAction extends WildcardArgsAction {

    @Override
    public String[] getParameterNames() {
        return new String[]{"filename"};
    }

    @Override
    public ActionResult process(PipelineContext<String> context) {
        File file = new File(context.getInputParams()[0]);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException fe) {
            return new ActionResult(false, "Cannot open input file '" + file.getAbsolutePath() + "'");
        }
        return handleFile(context, scanner);
    }

    public abstract ActionResult handleFile(PipelineContext<String> context, Scanner fileScanner);
}
