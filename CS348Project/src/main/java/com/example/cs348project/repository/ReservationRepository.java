package com.example.cs348project.repository;

import com.example.cs348project.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbc;

    public ReservationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean reserve(int roomId, String studentName, String start, String end) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM Reservation " +
                        "WHERE room_id = ? AND NOT (end_time <= ? OR start_time >= ?)",
                Integer.class, roomId, start, end
        );

        if (count != null && count > 0) {
            return false;
        }

        jdbc.update("INSERT INTO Reservation(room_id, student_name, start_time, end_time) VALUES (?, ?, ?, ?)",
                roomId, studentName, start, end);

        return true;
    }

    public List<Reservation> getAllReservations() {
        String sql = "SELECT reservation_id, room_id, student_name, start_time, end_time FROM Reservation";

        return jdbc.query(sql, (rs, rowNum) -> {
            Reservation r = new Reservation();
            r.setReservationId(rs.getInt("reservation_id"));
            r.setRoomId(rs.getInt("room_id"));
            r.setStudentName(rs.getString("student_name"));
            r.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
            r.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
            return r;
        });
    }

    public List<Reservation> getReservationsBetween(String start, String end) {
        String sql = "SELECT reservation_id, room_id, student_name, start_time, end_time " +
                "FROM Reservation WHERE start_time >= ? AND end_time <= ?";

        return jdbc.query(sql, new Object[]{start, end}, (rs, rowNum) -> {
            Reservation r = new Reservation();
            r.setReservationId(rs.getInt("reservation_id"));
            r.setRoomId(rs.getInt("room_id"));
            r.setStudentName(rs.getString("student_name"));
            r.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
            r.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
            return r;
        });
    }

    public List<Reservation> findByRoom(int roomId) {
        // Alternative: String sql = "SELECT * FROM Reservation WHERE room_id = " + roomId;
        return jdbc.query("SELECT * FROM Reservation WHERE room_id = ?",
                (rs, rowNum) -> {
                    Reservation r = new Reservation();
                    r.setReservationId(rs.getInt("reservation_id"));
                    r.setRoomId(rs.getInt("room_id"));
                    r.setStudentName(rs.getString("student_name"));
                    r.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    r.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                    return r;
                }, roomId);
    }
    public boolean deleteReservation(int reservationId) {
        int rows = jdbc.update("DELETE FROM Reservation WHERE reservation_id = ?", reservationId);
        return rows > 0;
    }

}
