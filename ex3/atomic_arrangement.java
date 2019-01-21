import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class atomic_arrangement {

  /* 目的ファイルを初期化 */
  public static void clean_file(String file) {
    // 記述部分
    try {
      FileWriter fw = new FileWriter(file);
      fw.write("");
      fw.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    double N;// 原子数
    int n = 1;// 出力時の原子数カウント用変数
    int lat;// 計算セル全体のうちの一辺あたりの格子の数
    double a;// 格子定数
    double A = 10 * Math.pow(0.1, 10);// オングストローム
    int a_types;// 原子のタイプ数
    String output_file;// 出力ファイル名
    String structure;// 金属の構造の定義
    String metal;// 金属の種類名

    // 今回の計算で必要な情報
    int center;// 計算セルの中心の原子が含まれる格子の番号(x,y,z軸それぞれに対称にする。)
    int center_id;// 計算セルの中心の原子のID

    // 本当は、ここでファイルを読み込んで上記の情報を全部格納したい。
    // 今回は、とりあえず全部手動代入で
    metal = "Cu";
    structure = "FCC";
    output_file = metal + ".lat";
    a_types = 1;
    a = 3.639;
    lat = 5;
    // N=Math.pow(lat+1,3)+3*lat*(lat+1)*lat;
    N = 4 * Math.pow(lat, 3) + 1;

    // 1辺が偶数個の時はど真ん中が存在。
    // 奇数個の時はど真ん中が存在しないため、原点よりにする。
    if (lat % 2 == 0) {
      center = (lat / 2);
    } else {
      center = ((lat - 1) / 2);
    }
    center_id = 0;// とりあえず初期化

    // 各繰り返し面の初期位置設定
    double[] vert = { 0, 0, 0 };
    double[] surf1 = { 0, 0.5 * a, 0.5 * a };
    double[] surf2 = { 0.5 * a, 0.5 * a, 0 };
    double[] surf3 = { 0.5 * a, 0, 0.5 * a };

    clean_file(output_file);// ファイルの作成(ある場合は中身を全消去)
    // 記述部分
    try {
      FileWriter fw = new FileWriter(output_file, true);
      PrintWriter pw = new PrintWriter(fw);

      /* セルの条件設定 */
      // pw.format("# atomic arrangement of %s metal :
      // %s\n",structure,metal);//一行目のコメント記述
      pw.format("#%s\n", metal);
      pw.println("");
      pw.format("%d atoms\n", (int) N);
      pw.format("%d atom types\n", a_types);
      pw.format("0.0 %f xlo xhi\n", a * lat);
      pw.format("0.0 %f ylo yhi\n", a * lat);
      pw.format("0.0 %f zlo zhi\n", a * lat);
      pw.println("");
      pw.println("Atoms");
      pw.println("");

      /* 原子座標の記述 */
      /* 格子内の原子の位置によって繰り返し数が違うため、ループ文を分けて処理 */

      // 格子の頂点原子の配置
      // kadai 2
      // for (int z = 0; z < lat; z++) {
      // for (int y = 0; y < lat; y++) {
      // for (int x = 0; x < lat; x++) {
      // // if(x == center && y == center && z == center){
      // // center_id = n;
      // // }
      // if (!(x == center && y == center && z == center)) {
      // pw.format("%d %d %f %f %f\n", n, a_types, vert[0] + x * a, vert[1] + y * a,
      // vert[2] + z * a);
      // n++;
      // }
      // pw.format("%d %d %f %f %f\n", n, a_types, surf1[0] + x * a, surf1[1] + y * a,
      // surf1[2] + z * a);
      // n++;
      // pw.format("%d %d %f %f %f\n", n, a_types, surf2[0] + x * a, surf2[1] + y * a,
      // surf2[2] + z * a);
      // n++;
      // pw.format("%d %d %f %f %f\n", n, a_types, surf3[0] + x * a, surf3[1] + y * a,
      // surf3[2] + z * a);
      // n++;
      // }
      // }
      // }

      // kadai3
      for (int z = 0; z < lat; z++) {
        for (int y = 0; y < lat; y++) {
          for (int x = 0; x < lat; x++) {
            // if(x == center && y == center && z == center){
            // center_id = n;
            // }
            if (!(x == center && y == center && z == center)) {
              pw.format("%d %d %f %f %f\n", n, a_types, vert[0] + x * a, vert[1] + y * a, vert[2] + z * a);
              n++;
            } else {
              pw.format("%d %d %f %f %f\n", n, a_types, vert[0] + x * a, vert[1] + y * a, vert[2] + z * a - a / 3);
              n++;
              pw.format("%d %d %f %f %f\n", n, a_types, vert[0] + x * a, vert[1] + y * a, vert[2] + z * a + a / 3);
              n++;
            }
            pw.format("%d %d %f %f %f\n", n, a_types, surf1[0] + x * a, surf1[1] + y * a, surf1[2] + z * a);
            n++;
            pw.format("%d %d %f %f %f\n", n, a_types, surf2[0] + x * a, surf2[1] + y * a, surf2[2] + z * a);
            n++;
            pw.format("%d %d %f %f %f\n", n, a_types, surf3[0] + x * a, surf3[1] + y * a, surf3[2] + z * a);
            n++;
          }
        }
      }

      /*
       * //格子内のx軸に垂直な面に存在する原子の配置 for(int z=0;z<lat;z++){ for(int y=0;y<lat;y++){
       * for(int x=0;x<=lat;x++){
       * pw.format("%d %d %f %f %f\n",n,a_types,surf1[0]+x*a,surf1[1]+y*a,surf1[2]+z*a
       * ); n++; } } }
       * 
       * //格子内のz軸に垂直な面に存在する原子の配置 for(int z=0;z<=lat;z++){ for(int y=0;y<lat;y++){
       * for(int x=0;x<lat;x++){
       * pw.format("%d %d %f %f %f\n",n,a_types,surf2[0]+x*a,surf2[1]+y*a,surf2[2]+z*a
       * ); n++; } } }
       * 
       * //格子内のy軸に垂直な面に存在する原子の配置 for(int z=0;z<lat;z++){ for(int y=0;y<=lat;y++){
       * for(int x=0;x<lat;x++){
       * pw.format("%d %d %f %f %f\n",n,a_types,surf3[0]+x*a,surf3[1]+y*a,surf3[2]+z*a
       * ); n++; } } }
       * 
       */
      pw.close();
      fw.close();

    } catch (IOException ex) {
      ex.printStackTrace();
    }

    // if(N!=(n-1)){
    // System.out.format("原子の個数が合いません。コードを見直してください。\n");
    // }else{
    System.out.format("作成原子総数N：%d\n", (int) N);
    System.out.format("作成原子総数n：%d\n", n - 1);
    System.out.format("中心原子番号：%d\n", center_id);
    // }
  }
}
