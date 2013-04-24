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
    long _variableId;
    NSString* _name;
    id _value;
    NSData* _changeDate;
    Task* _task;
    Session* _session;
    Event* _event;
    State* _state;
    
    
}

@property (nonatomic,assign) long variableId;
@property (nonatomic,retain) NSString* name;
@property (nonatomic,retain) id value;
@property (nonatomic,retain) NSData* changeDate;
@property (nonatomic,assign) Task* task;
@property (nonatomic,assign) Session* session;
@property (nonatomic,assign) Event* event;
@property (nonatomic,assign) State* state;

+(Variable*) copyFromVariable:(Variable*) variable;

+(Variable*) variable:(NSString*) name value:(id) value;

@end
