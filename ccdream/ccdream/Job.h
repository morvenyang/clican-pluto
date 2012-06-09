//
//  Job.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "State.h"
@interface Job : NSObject {
    long _jobId;
    NSString* _name;
    NSDate* _executeTime;
    int _repeatTime;
    int _repeatedTime;
    NSString* _repeatDuration;
    State* _state;
    NSString* _status;
}

@property (nonatomic,assign) long jobId;
@property (nonatomic,retain) NSString* name;
@property (nonatomic,retain) NSDate* executeTime;
@property (nonatomic,assign) int repeatTime;
@property (nonatomic,assign) int repeatedTime;
@property (nonatomic,retain) NSString* repeatDuration;
@property (nonatomic,retain) State* state;
@property (nonatomic,retain) NSString* status;

@end
