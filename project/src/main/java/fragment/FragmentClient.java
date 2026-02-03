package fragment;
import java.sql.*;
import java.util.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
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
            String url = "jdbc:postgresql://localhost:5432/fragment_" + i;
            String user = "postgres";
            String password = "admin";

            Connection conn = DriverManager.getConnection(url, user, password);
            connectionPool.put(i, conn);
        }
    }

    /**
     * TODO: Route the student to the correct shard and execute the INSERT.
     */
    public void insertStudent(String studentId, String name, int age, String email) {
        try {
            // Your code here:
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO: Route the grade to the correct shard and execute the INSERT.
     */
    public void insertGrade(String studentId, String courseId, int score) {
        int fragId = router.getFragmentId(studentId);
        Connection conn = connectionPool.get(fragId);
        String sql = "INSERT INTO Grade VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            ps.setInt(3, score);
            ps.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateGrade(String studentId, String courseId, int newScore) {
        int fragId = router.getFragmentId(studentId);
        Connection conn = connectionPool.get(fragId);

        String sql = "UPDATE Grade SET score = ? " +
                "WHERE student_id = ? AND course_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newScore);
            ps.setString(2, studentId);
            ps.setString(3, courseId);
            ps.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteStudentFromCourse(String studentId, String courseId) {
        int fragId = router.getFragmentId(studentId);
        Connection conn = connectionPool.get(fragId);

        String sql = "DELETE from Grade " +
                "WHERE student_id = ? AND course_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            ps.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * TODO: Fetch the student's name and email.
     */
    public String getStudentProfile(String studentId) {
        try {
            // Your code here
            return null; 
            
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    /**
     * TODO: Calculate the average score per department.
     */
    public String getAvgScoreByDept() {
        try {
            // Your code here
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    /**
     * TODO: Find all the students that have taken most number of courses
     */
    public String getAllStudentsWithMostCourses() {
        try {
            // Your code here
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public void closeConnections() {
        
    }
}
