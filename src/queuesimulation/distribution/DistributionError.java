/*
 * Copyright (C) 2016 Serhan Yılmaz
 *
 * This file is part of QueueSimulator
 * 
 * QueueSimulator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QueueSimulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package queuesimulation.distribution;

/**
 *
 * @author Serhan Yılmaz
 */
public class DistributionError {
    private final String error_message;
    private final String title;
    
    public DistributionError(String message, String title){
        this.error_message = message;
        this.title = title;
    }

    /**
     * @return the error message
     */
    public String getErrorMessage() {
        return error_message;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
}
