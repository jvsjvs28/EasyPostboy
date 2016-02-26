
package com.samtrest.easy_postboy;

import java.io.*;

public class RunCommand
{

    public RunCommand()
    {
    }

    public static void main(String args[])
    {
        String cm = "";
        for(int i = 0; i < args.length; i++)
        {
            if(i > 0)
                cm = cm + " ";
            cm = cm + args[i];
        }

        System.out.println("Command for execute: " + cm);
    }

    public static String exec(String cmm, String synchro)
    {
        String s = null;
        String cm = cmm;
        String rc = "";
        try
        {
            Process p = Runtime.getRuntime().exec(cm);
            if("Y".equals(synchro))
            {
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                while((s = stdInput.readLine()) != null)
                    rc = rc + s + '\n';
                while((s = stdError.readLine()) != null)
                    rc = rc + s + '\n';
            }
            rc = cm + "\t Executed.\nPID="+p.hashCode()+"\nRC="+p.exitValue()+"\n"+rc;
        }
        catch(Exception e)
        {
            rc = cm + "\nError[ " + e.getMessage() + " ]";
            System.out.println(rc);
        }
        return rc;
    }
    public static String exec(String []cmm, String synchro)
    {
        String s = null,comm = "";
        String [] cm = cmm;
        String rc = "";
        if (cm == null || cm.length == 0)
        	return rc;
        for (String string : cm) {
        	comm += string + " ";
		}
        
        try
        {
            Process p = Runtime.getRuntime().exec(cm);
            if("Y".equals(synchro))
            {
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                while((s = stdInput.readLine()) != null)
                    rc = rc + s + '\n';
                while((s = stdError.readLine()) != null)
                    rc = rc + s + '\n';
            }
            rc = comm + "\t Executed.\nPID="+p.hashCode()+"\nRC="+p.exitValue()+"\n"+rc;
        }
        catch(Exception e)
        {
            rc = comm + "\nError[ " + e.getMessage() + " ]";
            System.out.println(rc);
        }
        return rc;
    }
}

