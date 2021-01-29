package DUST.Communication;


import TestbenchController.Controller;

/**
 * The CommunicationHandler class will be responsible to handle the communication of the Testbench with other "agents" or applications.
 */
public class CommunicationHandler
{
    protected Controller controller;
    ICommunication communication;
    protected String channelName;
    protected int totalMessagescounter;
    protected int sizeOfmessage;
    protected int totalSizeOfAllMessagesSend;


    public CommunicationHandler(Controller controller, ICommunication DUSTcommunication, String channelName)
    {
        this.controller = controller;
        this.communication = DUSTcommunication;
        this.totalMessagescounter = 0;
        this.sizeOfmessage = 0;
        this.totalSizeOfAllMessagesSend = 0;
        this.channelName = channelName;
        this.register();
    }
    /**
    Hier gaat de DUST agent zich gaan subscribe op de DUST Channels die hij moet gaan uitlezen.
     */
    protected void register()
    {
        this.communication.registerListener(this.channelName,this::handleIncomingDUSTMessage);
    }

    protected void handleIncomingDUSTMessage(byte[] bytes)
    {
        //System.out.println("Message recieved!!! ");

        this.sizeOfmessage = bytes.length;
        this.totalSizeOfAllMessagesSend += this.sizeOfmessage;
        totalMessagescounter++;
    }

    public int getTotalMessagescounter()
    {
        return totalMessagescounter;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public int getTotalSizeOfAllMessagesSend()
    {
        return totalSizeOfAllMessagesSend;
    }

    public int getSizeOfmessage()
    {
        return sizeOfmessage;
    }

}
