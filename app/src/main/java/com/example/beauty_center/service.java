package com.example.beauty_center;

public class service {


    private String ServiceName;
    private int NumberOfReservation;
    private int Completed;
    private int Current;
    private int TicketNumber;
    private int Waiting;

        public service() {
        }

        public service(String serviceName) {
            ServiceName = serviceName;
            this.NumberOfReservation=0;
            this.Completed=0;
            this.Current=0;
        }



    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public int getNumberOfReservation() {
        return NumberOfReservation;
    }

    public void setNumberOfReservation(int numberOfReservation) {
        NumberOfReservation = numberOfReservation;
    }

    public int getCompleted() {
        return Completed;
    }

    public void setCompleted(int completed) {
        Completed = completed;
    }

    public int getCurrent() {
        return Current;
    }

    public void setCurrent(int current) {
        Current = current;
    }

    public int getTicketNumber() {
        return TicketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        TicketNumber = ticketNumber;
    }

    public int getWaiting() {
        return Waiting;
    }

    public void setWaiting(int waiting) {
        Waiting = waiting;
    }
}
