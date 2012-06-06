//
//  Variable.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Task.h"
#import "Session.h"
#import "Event.h"
#import "State.h"
@interface Variable : NSObject {
    int _variableId;
    NSString* _name;
    NSString* _value;
    NSData* _changeDate;
    Task* _task;
    Session* _session;
    Event* _event;
    State* _state;
    
    
}

@property (nonatomic,assign) int variableId;
@property (nonatomic,retain) NSString* name;
@property (nonatomic,retain) NSString* value;
@property (nonatomic,retain) NSData* changeDate;
@property (nonatomic,retain) Task* task;
@property (nonatomic,retain) Session* session;
@property (nonatomic,retain) Event* event;
@property (nonatomic,retain) State* state;

@end
