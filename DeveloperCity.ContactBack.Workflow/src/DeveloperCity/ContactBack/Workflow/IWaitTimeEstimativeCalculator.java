/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.DomainModel.Queue;

/**
 *
 * @author lbarbosa
 */
public interface IWaitTimeEstimativeCalculator {
    Queue getQueueObject(String phoneNumber);
    long calculate(Queue queueItem);
}
