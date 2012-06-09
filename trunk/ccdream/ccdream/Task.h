//
//  Task.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "State.h"

@interface Task : NSObject {
    long _taskId;
    NSString* _name;
    NSString* _type;
    NSString* _assignee;
    NSArray* _variables;
    State* _state;
    NSDate* _assignTime;
    NSDate* _completeTime;
    NSDate* _endTime;
}

@property (nonatomic,assign) long taskId;
@property (nonatomic,retain) NSString* name;
@property (nonatomic,retain) NSString* type;
@property (nonatomic,retain) NSString* assignee;
@property (nonatomic,retain) NSArray* variables;
@property (nonatomic,retain) State* state;
@property (nonatomic,retain) NSDate* assignTime;
@property (nonatomic,retain) NSDate* completeTime;
@property (nonatomic,retain) NSDate* endTime;

@end
