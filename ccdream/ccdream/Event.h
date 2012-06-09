//
//  Event.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "State.h"
@interface Event : NSObject {
    long _eventId;
    NSMutableArray* _variables;
    State* _state;
    NSDate* _completeTime;
    NSString* _eventType;
}

@property (nonatomic,assign) long eventId;
@property (nonatomic,retain) NSMutableArray* variables;
@property (nonatomic,retain) State* state;
@property (nonatomic,retain) NSDate* completeTime;
@property (nonatomic,retain) NSString* eventType;



@end
