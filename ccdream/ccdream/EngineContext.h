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

@interface EngineContext : NSObject {
    NSMutableDictionary* _sessionMap;
}

@property (nonatomic,retain) NSMutableDictionary* sessionMap;

-(StartState*) loadSession:(NSString*) name;

-(Session*) newSession:(NSString*) name forSponsor:(NSString*) sponsor;

-(Session*) querySesion:(int) sessionId;


@end
