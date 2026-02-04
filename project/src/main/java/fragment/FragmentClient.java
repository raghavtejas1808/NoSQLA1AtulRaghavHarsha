package fragment;
import java.sql.*;
import java.util.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.concurrent.ThreadLocalRandom;

public class FragmentClient {

    private Map<Integer, Connection> connectionPool;
    private Router router;
    private int numFragments;

    public FragmentClient(int numFragments) {
        this.numFragments = numFragments;
        this.router = new Router(numFragments);
        this.connectionPool = new HashMap<>();
    }

    /**
     * TODO: Initialize JDBC connections to all N Fragments.
     */
    public void setupConnections() throws SQLException {
        for (int i = 0; i < numFragments; i++) {
            String url = "jdbc:postgresql://localhost:5432/fragment1" + i;
            String user = "postgres";
            String password = "root";

            Connection conn = DriverManager.getConnection(url, user, password);
            connectionPool.put(i, conn);
        }
    }

    /**
     * TODO: Route the student to the correct shard and execute the INSERT.
     */
    public void insertStudent(String studentId, String name, int age, String email) {
            // Your code here:
                int fragId = router.getFragmentId(studentId);
                Connection conn = connectionPool.get(fragId);

                String sql = "INSERT INTO Student VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, studentId);
                    ps.setString(2, name);
                    ps.setInt(3, age);
                    ps.setString(4, email);
                    ps.executeUpdate();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
    }

    /**
     * TODO: Route the grade to the correct shard and execute the INSERT.
     */
    public void insertGrade(String studentId, String courseId, int score) {
        try {
            // Your code here
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateGrade(String studentId, String courseId, int newScore) {
        try {
		// Your code here:
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteStudentFromCourse(String studentId, String courseId) {
        try {
	// Your code here:
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
      * TODO: Fetch the student's name and email.
      */
    public String getStudentProfile(String studentId){
        int fragId = router.getFragmentId(studentId);
        Connection conn = connectionPool.get(fragId);

        String sql = "SELECT name,email from Student where student_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1,studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("name") + "," + rs.getString("email");
            }
        }
        catch (Exception e) {
             e.printStackTrace();
             return "ERROR";
         }
         return null;
     }


     /**
      * TODO: Calculate the average score per department.
      */
     public String getAvgScoreByDept() {
        int fragmentId = ThreadLocalRandom.current().nextInt(numFragments);
        Connection conn = connectionPool.get(fragmentId);

        String sql =
                "SELECT c.department, AVG(g.score) AS avg_score " +
                        "FROM Grade g JOIN Course c ON g.course_id = c.course_id " +
                        "GROUP BY c.department";

        StringBuilder result = new StringBuilder();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.append(rs.getString("department")).append("-")
                        .append("AverageScore:").append(String.format("%.1f", rs.getDouble("avg_score")))
                        .append(";\t");
            }

            if (!result.isEmpty())
                result.setLength(result.length() - 1);

            return result.toString();
        }

        catch (Exception e) {
             e.printStackTrace();
             return "ERROR";
         }
     }

     /**
      * TODO: Find all the students that have taken most number of courses
      */
     public String getAllStudentsWithMostCourses() {
        int fragmentId = ThreadLocalRandom.current().nextInt(numFragments);
        Connection conn = connectionPool.get(fragmentId);

        String sql =
                "SELECT student_id, COUNT(course_id) as NCourses FROM Grade " +
                        "GROUP BY student_id HAVING COUNT(course_id) = " +
                        "(SELECT MAX(cnt) FROM" +
                        " (SELECT COUNT(course_id) AS cnt FROM Grade GROUP BY student_id))";
        StringBuilder result = new StringBuilder();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.append("Student_ID:").append(rs.getString("student_id"))
                        .append("\t").append("N(Courses):").append(rs.getInt("NCourses"))
                        .append("\n");
            }
            return result.toString();
        }

        catch (Exception e) {
             e.printStackTrace();
             return "ERROR";
         }
     }
    public void closeConnections() throws SQLException {
        for (Connection conn : connectionPool.values()){
            conn.close();
        }
     }

 }
