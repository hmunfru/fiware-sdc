/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.client.model.Tasks;
import com.telefonica.euro_iaas.sdc.client.services.TaskService;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;


import static com.telefonica.euro_iaas.sdc.client.ClientConstants.PRODUCT_INSTANCE_PATH;

/**
 * Default TaskService implementation.
 * 
 * @author Sergio Arroyo
 */
public class TaskServiceImpl extends AbstractBaseService implements TaskService {

    private Long waitingPeriod = 5000l; // every 5*iter seconds try again
    private Long maxWaiting = 600000l; // max 10 minutes waiting

    /**
     * @param client
     * @param baseHost
     * @param type
     */
    public TaskServiceImpl(Client client, String baseHost, String type) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
    }

    /**
     * @param client
     * @param baseHost
     * @param type
     * @param waitingPeriod
     * @param maxWaiting
     */
    public TaskServiceImpl(Client client, String baseHost, String type, Long waitingPeriod, Long maxWaiting) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
        this.waitingPeriod = waitingPeriod;
        this.maxWaiting = maxWaiting;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task load(String vdc, Long id) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.TASK_PATH, vdc, id);
        return this.load(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task load(String url) {
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).get(Task.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findAll(Integer page, Integer pageSize, String orderBy, String orderType,
            List<TaskStates> states, String resource, String owner, Date fromDate, Date toDate, String vdc) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.BASE_TASK_PATH, vdc);
        WebResource wr = getClient().resource(url);
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams = addParam(searchParams, "page", page);
        searchParams = addParam(searchParams, "pageSize", pageSize);
        searchParams = addParam(searchParams, "orderBy", orderBy);
        searchParams = addParam(searchParams, "orderType", orderType);
        searchParams = addParam(searchParams, "fromDate", fromDate);
        searchParams = addParam(searchParams, "toDate", toDate);
        searchParams = addParam(searchParams, "owner", owner);
        searchParams = addParam(searchParams, "result", resource);
        if (states != null) {
            for (TaskStates state : states) {
                searchParams = addParam(searchParams, "states", state);
            }
        }
        return wr.queryParams(searchParams).accept(getType()).get(Tasks.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task waitForTask(String url) throws MaxTimeWaitingExceedException {
        try {
            Task task = this.load(url);
            Integer count = 1;
            while (task.getStatus() == TaskStates.RUNNING) {
                Thread.sleep(count * waitingPeriod);
                count = count + 1;
                task = this.load(url);
                if (isTimeExceed(task.getStartTime())) {
                    throw new MaxTimeWaitingExceedException();
                }
            }
            return task;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Calculates the time passed since task started and check if is biggest than the maximum
     */
    private Boolean isTimeExceed(Date startedDate) {
        return (new Date().getTime() - startedDate.getTime()) > maxWaiting;
    }

    @Override
    public List<Task> findAllByProduct(String vdc, String productName) {
        String resource = MessageFormat.format(getBaseHost() + PRODUCT_INSTANCE_PATH, vdc, productName);
        return findAll(null, null, null, null, null, resource, null, null, null, vdc);
    }
}
