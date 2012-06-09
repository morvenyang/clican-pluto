//
//  EngineContext.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Session.h"
#import "State.h"
#import "DefaultState.h"
#import "StartState.h"
#import "EndState.h"
#import "TaskState.h"
#import "XMLParserDelegate.h"
#import "IState.h"

@interface EngineContext : NSObject {
    NSMutableDictionary* _sessionMap;
    NSMutableDictionary* _startStateMap;
    NSMutableDictionary* _sessionStateMap;
    long _atomicLong;
}

@property (nonatomic,retain) NSMutableDictionary* sessionMap;
@property (nonatomic,retain) NSMutableDictionary* startStateMap;
@property (nonatomic,retain) NSMutableDictionary* sessionStateMap;
+(EngineContext*) sharedEngineContext;

-(long) getAndAddAtomicLong;

-(StartState*) loadSession:(NSString*) name;

-(Session*) newSession:(NSString*) name forSponsor:(NSString*) sponsor;

-(Session*) querySesion:(long) sessionId;

-(State*) findStateById:(long) stateId sessionId:(long) sessionId;

-(IState*) getState:(NSString*) sessionName stateName:(NSString*) stateName;
@end
