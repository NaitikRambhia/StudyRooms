package com.example.cs348project.repository;

import com.example.cs348project.model.StudyRoom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomRepository {

    private final JdbcTemplate jdbc;

    public RoomRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<StudyRoom> getAllRooms() {
        String sql = "SELECT room_id, name, capacity FROM StudyRoom";
        return jdbc.query(sql, (rs, rowNum) -> {
            StudyRoom r = new StudyRoom();
            r.setRoomId(rs.getInt("room_id"));
            r.setName(rs.getString("name"));
            r.setCapacity(rs.getInt("capacity"));
            return r;
        });
    }

    public boolean addRoom(int roomId, String name, int capacity) {

        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM StudyRoom WHERE room_id = ?",
                Integer.class, roomId
        );

        if (count != null && count > 0) {
            return false;
        }

        jdbc.update("INSERT INTO StudyRoom(room_id, name, capacity) VALUES (?, ?, ?)",
                roomId, name, capacity);

        return true;
    }



    public String deleteRoom(int roomId) {

        Integer exists = jdbc.queryForObject(
                "SELECT COUNT(*) FROM StudyRoom WHERE room_id = ?",
                Integer.class, roomId
        );

        if (exists == null || exists == 0) {
            return "NOT_FOUND";
        }

        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM Reservation WHERE room_id = ?",
                Integer.class, roomId
        );

        if (count != null && count > 0) {
            return "HAS_RESERVATIONS";
        }

        jdbc.update("DELETE FROM StudyRoom WHERE room_id = ?", roomId);
        return "DELETED";
    }


    public boolean editRoom(int roomId, String newName, int newCapacity) {

        // Check if room exists
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM StudyRoom WHERE room_id = ?",
                Integer.class, roomId
        );

        if (count == null || count == 0) {
            return false; // room doesn't exist
        }

        jdbc.update("UPDATE StudyRoom SET name = ?, capacity = ? WHERE room_id = ?",
                newName, newCapacity, roomId);

        return true;
    }



}
