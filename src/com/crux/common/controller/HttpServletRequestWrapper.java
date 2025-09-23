/***********************************************************************
 * Module:  com.crux.common.controller.HttpServletRequestWrapper
 * Author:  Denny Mahendra
 * Created: Jul 5, 2005 3:09:46 PM
 * Purpose:
 ***********************************************************************/

package com.crux.common.controller;

import com.oreilly.servlet.MultipartRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

public class HttpServletRequestWrapper implements HttpServletRequest{
   private HttpServletRequest rq;
   private MultipartRequest mp;
   private Hashtable parameters;

   public HttpServletRequestWrapper(HttpServletRequest rq, MultipartRequest mp) throws Exception {
      this.rq = rq;
      this.mp = mp;

      rq.setAttribute("MULTIPART",new MultiPartWrapper(mp));

      /*Part part;

      while ((part=mp.readNextPart()) != null) {
         if (part instanceof FilePart) {

         }
         else if (part instanceof ParamPart) {
            ParamPart paramPart = (ParamPart) part;

         }
      }*/
   }

   public HttpServletRequestWrapper(HttpServletRequest httpServletRequest, Hashtable parameters) {
      this.rq = httpServletRequest;
      this.parameters = parameters;
   }

   public String getAuthType() {
      return rq.getAuthType();
   }

   public Cookie[] getCookies() {
      return rq.getCookies();
   }

   public long getDateHeader(String s) {
      return rq.getDateHeader(s);
   }

   public String getHeader(String s) {
      return rq.getHeader(s);
   }

   public Enumeration getHeaders(String s) {
      return rq.getHeaders(s);
   }

   public Enumeration getHeaderNames() {
      return rq.getHeaderNames();
   }

   public int getIntHeader(String s) {
      return rq.getIntHeader(s);
   }

   public String getMethod() {
      return rq.getMethod();
   }

   public String getPathInfo() {
      return rq.getPathInfo();
   }

   public String getPathTranslated() {
      return rq.getPathTranslated();
   }

   public String getContextPath() {
      return rq.getContextPath();
   }

   public String getQueryString() {
      return rq.getQueryString();
   }

   public String getRemoteUser() {
      return rq.getRemoteUser();
   }

   public boolean isUserInRole(String s) {
      return rq.isUserInRole(s);
   }

   public Principal getUserPrincipal() {
      return rq.getUserPrincipal();
   }

   public String getRequestedSessionId() {
      return rq.getRequestedSessionId();
   }

   public String getRequestURI() {
      return rq.getRequestURI();
   }

   public StringBuffer getRequestURL() {
      return rq.getRequestURL();
   }

   public String getServletPath() {
      return rq.getServletPath();
   }

   public HttpSession getSession(boolean b) {
      return rq.getSession(b);
   }

   public HttpSession getSession() {
      return rq.getSession();
   }

   public boolean isRequestedSessionIdValid() {
      return rq.isRequestedSessionIdValid();
   }

   public boolean isRequestedSessionIdFromCookie() {
      return rq.isRequestedSessionIdFromCookie();
   }

   public boolean isRequestedSessionIdFromURL() {
      return rq.isRequestedSessionIdFromUrl();
   }

   public boolean isRequestedSessionIdFromUrl() {
      return rq.isRequestedSessionIdFromURL();
   }

   public Object getAttribute(String s) {
      return rq.getAttribute(s);
   }

   public Enumeration getAttributeNames() {
      return rq.getAttributeNames();
   }

   public String getCharacterEncoding() {
      return rq.getCharacterEncoding();
   }

   public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
      rq.setCharacterEncoding(s);
   }

   public int getContentLength() {
      return rq.getContentLength();
   }

   public String getContentType() {
      return rq.getContentType();
   }

   public ServletInputStream getInputStream() throws IOException {
      return rq.getInputStream();
   }

   public String getParameter(String s) {
      if (parameters!=null) {

         Object o = parameters.get(s);

         if (o==null) return null;

         if (o instanceof String) return (String) o;

         String[] l = (String [] ) o;

         if (l==null || l.length<1) return null;

         return l [0];
      }
      return mp.getParameter(s);
   }

   public Enumeration getParameterNames() {
      if (parameters!=null) return parameters.keys();
      return mp.getParameterNames();
   }

   public String[] getParameterValues(String s) {
      if (parameters!=null) return (String[]) parameters.get(s);
      return mp.getParameterValues(s);
   }

   public Map getParameterMap() {
      return rq.getParameterMap();
   }

   public String getProtocol() {
      return rq.getProtocol();
   }

   public String getScheme() {
      return rq.getScheme();
   }

   public String getServerName() {
      return rq.getServerName();
   }

   public int getServerPort() {
      return rq.getServerPort();
   }

   public BufferedReader getReader() throws IOException {
      return rq.getReader();
   }

   public String getRemoteAddr() {
      return rq.getRemoteAddr();
   }

   public String getRemoteHost() {
      return rq.getRemoteHost();
   }

   public void setAttribute(String s, Object o) {
      rq.setAttribute(s,o);
   }

   public void removeAttribute(String s) {
      rq.removeAttribute(s);
   }

   public Locale getLocale() {
      return rq.getLocale();
   }

   public Enumeration getLocales() {
      return rq.getLocales();
   }

   public boolean isSecure() {
      return rq.isSecure();
   }

   public RequestDispatcher getRequestDispatcher(String s) {
      return rq.getRequestDispatcher(s);
   }

   public String getRealPath(String s) {
      return rq.getRealPath(s);
   }
}
