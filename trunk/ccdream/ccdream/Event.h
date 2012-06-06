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
    int _eventId;
    NSArray* _variables;
    State* _state;
    NSDate* _completeTime;
    NSString* _eventType;
}

@property (nonatomic,assign) int eventId;
@property (nonatomic,retain) NSArray* variables;
@property (nonatomic,retain) State* state;
@property (nonatomic,retain) NSDate* completeTime;
@property (nonatomic,retain) NSString* eventType;



@end
