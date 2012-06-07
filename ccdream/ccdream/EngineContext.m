//
//  EngineContext.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "EngineContext.h"

@implementation EngineContext

@synthesize sessionMap = _sessionMap;

-(void) loadConfig:(NSString*) name {
        
    
}




-(Session*) newSession:(NSString*) name forSponsor:(NSString*) sponsor{
    Session* session = [[[Session alloc] init] autorelease];
    session.sponsor = sponsor;
    return session;
}

-(Session*) querySesion:(int) sessionId{
    Session* session = [self.sessionMap objectForKey:[NSString stringWithFormat:@"%i",sessionId]];
    return session;
}


- (void)dealloc {
    [_sessionMap release];
    _sessionMap = nil;
    [super dealloc];
}
@end
