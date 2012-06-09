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
    long _stateId;
    NSString* _name;
    int _value;
    NSString* _status;
    Session* _session;
    NSMutableArray* _jobs;
    NSMutableArray* _tasks;
    NSMutableArray* _events;
    NSMutableArray* _variables;
    NSDate* _startTime;
    NSDate* _endTime;
}


@property (nonatomic,assign) long stateId;
@property (nonatomic,retain) NSString* name;
@property (nonatomic,assign) int value;
@property (nonatomic,retain) NSString* status;
@property (nonatomic,retain) Session* session;
@property (nonatomic,retain) NSMutableArray* jobs;
@property (nonatomic,retain) NSMutableArray* tasks;
@property (nonatomic,retain) NSMutableArray* events;
@property (nonatomic,retain) NSMutableArray* variables;
@property (nonatomic,retain) NSDate* startTime;
@property (nonatomic,retain) NSDate* endTime;


@end
