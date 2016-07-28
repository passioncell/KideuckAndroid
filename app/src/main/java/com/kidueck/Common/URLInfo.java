package com.kidueck.Common;

/**
 * Created by system777 on 2016-06-25.
 */
public class URLInfo {
    public final static String WEB_SERVER_IP = "http://dayot.seobuchurch.or.kr"; //루트 도메인
    public final static String Member_Join = WEB_SERVER_IP + "/Member/Join"; //가입
    public final static String Member_Login = WEB_SERVER_IP + "/Member/Login"; //로그인
    public final static String Member_GetUserPrimaryKey = WEB_SERVER_IP + "/Member/GetUserPrimaryKey"; //기본키 가져오기
    public final static String Member_CheckJoin = WEB_SERVER_IP + "/Member/CheckJoin"; //가입했는지 체크 안했으면 가입시키기기
    public final static String Posting_GetPostingList = WEB_SERVER_IP + "/Posting/GetPostingList"; //피드 가져오기
    public final static String Posting_GetHotPostingList = WEB_SERVER_IP + "/Posting/GetHotPostingList"; //피드 HOT 가져오기
    public final static String Posting_GetMyPostingList = WEB_SERVER_IP + "/Posting/GetMyPostingList"; //피드 MY 가져오기
    public final static String Posting_VotePosting2 = WEB_SERVER_IP + "/Posting/VotePosting2"; //투표
    public final static String Notice_GetNoticeList2 = WEB_SERVER_IP + "/Notice/GetNoticeList2"; //알림 리스트 가져오기
    public final static String Comment_GetCommentList = WEB_SERVER_IP + "/Comment/GetCommentList2"; //댓글 가져오기
    public final static String Posting_GetDetailPosting = WEB_SERVER_IP + "/Posting/GetDetailPosting0"; //디테일 정보 가져오기(신)
    public final static String Comment_WriteComment2 = WEB_SERVER_IP + "/Comment/WriteComment2"; //댓글 쓰기
    public final static String Notice_GetNewNoticeCnt = WEB_SERVER_IP + "/Notice/GetNewNoticeCnt"; //최신 알림 갯수 가져오기
    public final static String Posting_WritePosting2 = WEB_SERVER_IP + "/Posting/WritePosting2"; //최신 알림 갯수 가져오기
    public final static String Setting_GetLatestVersionName = WEB_SERVER_IP + "/Setting/GetLatestVersionName"; //최신 버전네임 가져오기
    public final static String Setting_WriteInquiry = WEB_SERVER_IP + "/Setting/WriteInquiry"; //문의사항 전송
    public final static String Member_GetUserPoint = WEB_SERVER_IP + "/Member/GetUserPoint"; // GET 유저 포인트트
    public final static String Posting_UploadImage = WEB_SERVER_IP + "/Posting/WritePostingWithImage"; // 포스팅 이미지 전송


    public String getWebServerIp(){
        return WEB_SERVER_IP;
    }

    public String getPostImgUploadUrl(){
        return WEB_SERVER_IP + "/upload/post/";
    }
}