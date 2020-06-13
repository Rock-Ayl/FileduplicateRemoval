import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created By Rock-Ayl on 2020-06-13
 * 删除某个path下所有MD5大小相同的文件
 */
public class Main {

    //要去重文件的目录
    public static final String dirPath = "/Users/ayl/Pictures/半佛老师表情包/";

    /**
     * 执行
     *
     * @param args
     */
    public static void main(String[] args) {
        //获取目录对象
        File dir = new File(dirPath);
        //如果不存在或是个文件
        if (!dir.exists() || dir.isFile()) {
            System.out.println("不存在文件目录或者文件目录是个文件,无法执行操作.");
            System.exit(-1);
        }
        //获取该路径下所有文件(包括子文件夹下的)
        List<String> filePathList = getFilePathList(dir);
        //判空
        if (CollectionUtils.isEmpty(filePathList)) {
            System.out.println("文件夹下无文件.");
            System.exit(-1);
        }

        System.out.println("去重前拥有文件数:" + filePathList.size());
        //一个map,key是文件对应的md5,value是文件地址
        HashMap<String, String> fileMap = new HashMap<>();
        //删除次数
        int delCount = 0;
        //循环所有文件了
        for (String filePath : filePathList) {
            //当前文件对象
            File file = new File(filePath);
            //如果存在对象并且是个文件
            if (file.exists() && file.isFile()) {
                //获取文件md5
                String md5 = getFileMd5(filePath);
                //判空
                if (StringUtils.isEmpty(md5)) {
                    //中断
                    continue;
                }
                //如果已存在该文件
                if (fileMap.containsKey(md5)) {
                    System.out.println("删除中....");
                    //删除该重复的文件
                    FileUtils.deleteQuietly(file);
                    //记录++
                    delCount++;
                } else {
                    //记录该文件信息
                    fileMap.put(md5, filePath);
                }
            }
        }
        System.out.println("去重完毕,总共删除文件:" + delCount);
    }

    /**
     * 获取文件MD5值
     * 经过测试:消耗较小内存下,6.3G文件需要18秒左右转化时间
     *
     * @param filePath 文件路径 eg:"/Users/ayl/Movies/电影-泰坦尼克号.mp4"
     * @return MD5值
     * @throws IOException
     */
    public static String getFileMd5(String filePath) {
        try {
            //获取并返回
            return DigestUtils.md5Hex(new FileInputStream(filePath));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取该path下所有文件完整path(包括子文件夹下的)
     *
     * @param path eg: /Users/ayl/图表
     * @return eg: ["/Users/ayl/图表/Icon/picture.jpg"]
     */
    public static List<String> getFilePathList(File path) {
        //初始化文件列表
        List<String> fileList = new ArrayList<>();
        //获取文件对象组
        File[] fs = path.listFiles();
        //判空
        if (fs != null && fs.length > 0) {
            //循环
            for (File f : fs) {
                //如果是目录
                if (f.isDirectory()) {
                    //若是目录,递归该目录
                    List<String> childFileList = getFilePathList(f);
                    //判空
                    if (CollectionUtils.isNotEmpty(childFileList)) {
                        //组装所有子文件
                        fileList.addAll(childFileList);
                    }
                }
                //如果是文件
                if (f.isFile()) {
                    //组装
                    fileList.add(f.toString());
                }
            }
        }
        return fileList;
    }


}
