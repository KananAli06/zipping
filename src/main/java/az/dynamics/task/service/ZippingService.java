package az.dynamics.task.service;

import az.dynamics.task.entity.Process;
import az.dynamics.task.entity.Task;
import az.dynamics.task.exception.FileNotFoundException;
import az.dynamics.task.model.request.CreateZipFileRequestDto;
import az.dynamics.task.model.response.CreateZipFileResponseDto;
import az.dynamics.task.model.response.ZippingInfoDto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static az.dynamics.task.model.Status.COMPLETED;
import static az.dynamics.task.model.Status.FAILED;

/**
 * @author Kanan
 */
@Service
public class ZippingService {
    private final TaskService taskService;
    private final ProcessService processService;

    public ZippingService(TaskService taskService,
                          ProcessService processService) {
        this.taskService = taskService;
        this.processService = processService;
    }

    public CreateZipFileResponseDto zipping(CreateZipFileRequestDto request) {
        final String path = request.getPath();

        File source = new File(path);
        if (!source.exists())
            throw new FileNotFoundException(path);

        final String fileName = source.getName();
        final String zipFilePath = generateZipFilePath(path);

        Process process = processService.create();
        Task task = taskService.create(process, path);

        new Thread(() -> {
            try (FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
                 ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
                zipFile(source, fileName, zipOutputStream);
                Thread.sleep(30000);
                processService.update(process.getId(), COMPLETED);
                taskService.updatePathOfZipFile(task.getId(), zipFilePath);
            } catch (IOException e) {
                processService.update(process.getId(), FAILED);
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        CreateZipFileResponseDto responseDto = new CreateZipFileResponseDto();
        responseDto.setId(task.getId());
        return responseDto;
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();

            if (children != null) {
                for (File childFile : children) {
                    zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
                }
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public String getFilePathWithoutExtension(String source) {
        return source.replaceFirst("[.][^.]+$", "");
    }

    public String generateZipFilePath(final String fileName) {
        final String filePathWithoutExtension = getFilePathWithoutExtension(fileName);
        return filePathWithoutExtension.concat(".zip");
    }

    public ZippingInfoDto getZippingInfo(final Integer id) {
        return taskService.getStageOfZippingTask(id);
    }

}
