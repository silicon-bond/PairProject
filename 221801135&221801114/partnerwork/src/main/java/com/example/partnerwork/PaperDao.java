package com.example.partnerwork;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaperDao {
    private static PaperDao paperDao;
    private List<Paper> paperList;
    
    private PaperDao(){}
    
    public static PaperDao getInstance(){
        if (paperDao == null){
            paperDao = new PaperDao();
        }
        return paperDao;
    }

    public int getTotal(){
        return paperList.size();
    }

    public Paper find(int id){
        Paper paper = new Paper();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select * from paper where id="+id;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                paper.setId(rs.getInt("id"));
                paper.setTitle(rs.getString("title"));
                paper.setAbstractText(rs.getString("abstract"));
                paper.setKeywords(rs.getString("keywords"));
                paper.setTags();
                paper.setTagList();
                paper.setDoiLink(rs.getString("doiLink"));
                paper.setPublicationDate(rs.getInt("publicationDate"));
                paper.setConference(rs.getString("conference"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paper;
    }

    public List<Paper> find(String findString){
        if (findString.trim().equals("")){
            paperList = null;
            return list(1, 8);
        }
        findString = findString.trim().toLowerCase();
        paperList = new ArrayList<Paper>();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select * from paper where lower(title) like '%"+findString+"%'";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Paper paper = new Paper();
                paper.setId(rs.getInt("id"));
                paper.setTitle(rs.getString("title"));
                paper.setAbstractText(rs.getString("abstract"));
                paper.setKeywords(rs.getString("keywords"));
                paper.setTags();
                paper.setTagList();
                paper.setDoiLink(rs.getString("doiLink"));
                paper.setPublicationDate(rs.getInt("publicationDate"));
                paper.setConference(rs.getString("conference"));
                paperList.add(paper);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list(1, 8);
    }

    public List<Paper> findByTag(String tag){
        tag = tag.trim().toLowerCase();
        paperList = new ArrayList<Paper>();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select * from paper where lower(keywords) like '%" + tag + "%'";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Paper paper = new Paper();
                paper.setId(rs.getInt("id"));
                paper.setTitle(rs.getString("title"));
                paper.setAbstractText(rs.getString("abstract"));
                paper.setKeywords(rs.getString("keywords"));
                paper.setTags();
                paper.setTagList();
                paper.setDoiLink(rs.getString("doiLink"));
                paper.setPublicationDate(rs.getInt("publicationDate"));
                paper.setConference(rs.getString("conference"));
                paperList.add(paper);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list(1, 8);
    }
    
    public void add(Paper bean){
        String sql = "insert into paper values(null ,? ,? ,? ,? ,? ,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, bean.getTitle());
            ps.setString(2, bean.getAbstractText());
            ps.setString(3, bean.getKeywords());
            ps.setString(4, bean.getDoiLink());
            ps.setInt(5, bean.getPublicationDate());
            ps.setString(6, bean.getConference());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public void add(List<Paper> list){
        String sql = "insert into paper values(null ,? ,? ,? ,? ,? ,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Paper bean : list){
                ps.setString(1, bean.getTitle());
                ps.setString(2, bean.getAbstractText());
                ps.setString(3, bean.getKeywords());
                ps.setString(4, bean.getDoiLink());
                ps.setInt(5, bean.getPublicationDate());
                ps.setString(6, bean.getConference());
                ps.execute();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    bean.setId(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Paper> list(){
        paperList = new ArrayList<Paper>();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select * from paper";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Paper paper = new Paper();
                paper.setId(rs.getInt("id"));
                paper.setTitle(rs.getString("title"));
                paper.setAbstractText(rs.getString("abstract"));
                paper.setKeywords(rs.getString("keywords"));
                paper.setTags();
                paper.setDoiLink(rs.getString("doiLink"));
                paper.setPublicationDate(rs.getInt("publicationDate"));
                paper.setConference(rs.getString("conference"));
                paperList.add(paper);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paperList;
    }

    public List<Paper> list(int start, int count){
        if (paperList == null){
            list();
        }
        List<Paper> list = new ArrayList<Paper>();
        if (start * count > paperList.size()){
            for (int i = (start - 1) * count;i < paperList.size();i ++){
                list.add(paperList.get(i));
            }
        }
        else{
            list = paperList.subList((start - 1) * count , (start - 1) * count + count);
        }
        return list;
    }
}
