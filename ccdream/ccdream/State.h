//
//  State.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Session.h"
@interface State : NSObject {
    int _stateId;
    NSString* _name;
    int _value;
    NSString* _status;
    Session* _session;
    NSArray* _jobs;
    NSArray* _tasks;
    NSArray* _events;
    NSArray* _variables;
    NSDate* _startTime;
    NSDate* _endTime;
}


@property (nonatomic,assign) int stateId;
@property (nonatomic,retain) NSString* name;
@property (nonatomic,assign) int value;
@property (nonatomic,retain) NSString* status;
@property (nonatomic,retain) Session* session;
@property (nonatomic,retain) NSArray* jobs;
@property (nonatomic,retain) NSArray* tasks;
@property (nonatomic,retain) NSArray* events;
@property (nonatomic,retain) NSArray* variables;
@property (nonatomic,retain) NSDate* startTime;
@property (nonatomic,retain) NSDate* endTime;


@end
