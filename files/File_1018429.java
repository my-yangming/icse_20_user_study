package com.xiaolyuh.service.impl;

import com.github.xiaolyuh.annotation.*;
import com.xiaolyuh.entity.Person;
import com.xiaolyuh.repository.PersonRepository;
import com.xiaolyuh.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    PersonRepository personRepository;

    @Override
    @CachePut(value = "people", key = "#person.id", depict = "用户信�?�缓存")
    public Person save(Person person) {
        Person p = personRepository.save(person);
        logger.info("为id�?key为:" + p.getId() + "数�?��?�了缓存");
        return p;
    }

    @Override
    @CacheEvict(value = "people", key = "#id")//2
    public void remove(Long id) {
        logger.info("删除了id�?key为" + id + "的数�?�缓存");
        //这里�?�?�实际删除�?作
    }

    @Override
    @CacheEvict(value = "people", allEntries = true)//2
    public void removeAll() {
        logger.info("删除了所有缓存的数�?�缓存");
        //这里�?�?�实际删除�?作
    }

    @Override
    @Cacheable(value = "'people' + ':' + #person.id", key = "#person.id", depict = "用户信�?�缓存",
            firstCache = @FirstCache(expireTime = 4),
            secondaryCache = @SecondaryCache(expireTime = 15, preloadTime = 8, forceRefresh = true))
    public Person findOne(Person person) {
        Person p = personRepository.findOne(Example.of(person));
        logger.info("为id�?key为:" + p.getId() + "数�?��?�了缓存");
        return p;
    }

    @Override
    @Cacheable(value = "people1", key = "#person.id", depict = "用户信�?�缓存1",
            firstCache = @FirstCache(expireTime = 4),
            secondaryCache = @SecondaryCache(expireTime = 15, preloadTime = 8, forceRefresh = true))
    public Person findOne1(Person person) {
        Person p = personRepository.findOne(Example.of(person));
        logger.info("为id�?key为:" + p.getId() + "数�?��?�了缓存");
        return p;
    }
}
