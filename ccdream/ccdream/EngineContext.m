//
//  EngineContext.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "EngineContext.h"
#import "XMLParserDelegate.h"

@implementation EngineContext

@synthesize sessionMap = _sessionMap;
@synthesize startStateMap = _startStateMap;
@synthesize sessionStateMap = _sessionStateMap;

static EngineContext *sharedEngineContext = nil;

-(id) init {
    self = [super init];
    if(self){
        _atomicLong = [[NSDate date] timeIntervalSince1970];
    }
    return self;
}

+(EngineContext*) sharedEngineContext{
    @synchronized(self) {
		if (sharedEngineContext == nil){
            sharedEngineContext = [[self alloc] init]; // assignment not done here
        }
	}
	return sharedEngineContext;
}

-(long) getAndAddAtomicLong{
    @synchronized(self){
        _atomicLong++;
        return _atomicLong;
    }
}

-(StartState*) loadSession:(NSString*) name {
    StartState* startState = [self.startStateMap objectForKey:name];
    if (startState!=nil) {
        return startState;
    }else{
        NSBundle* bundle = [NSBundle bundleWithPath:[name stringByAppendingString:@".xml"]];
        NSXMLParser *xmlParser = [[NSXMLParser alloc] initWithContentsOfURL:bundle.bundleURL];
        //Initialize the delegate.
        XMLParserDelegate* parser = [[XMLParserDelegate alloc] init];
        //Set delegate
        [xmlParser setDelegate:parser];
        BOOL success = [xmlParser parse];
        if(success){
            CCLOG(@"parse %@.xml successfully",name);
            startState = parser.startState;
            [self.startStateMap setValue:startState forKey:name];
            [self.sessionStateMap setValue:parser.statesMap forKey:name];
            return startState;
        }else{
            CCLOGERROR(@"parse %@.xml successfully",name);
            return nil;
        }
    }
}




-(Session*) newSession:(NSString*) name forSponsor:(NSString*) sponsor{
    StartState* startState = [self loadSession:name];
    if(startState!=nil){
        Session* session = [[[Session alloc] init] autorelease];
        session.sessionId = [self getAndAddAtomicLong];
        session.sponsor = sponsor;
        [self.sessionMap setValue:session forKey:[NSString stringWithFormat:@"@i",session.sessionId]];
        return session;
    }else{
        return nil;
    }
    
}

-(Session*) querySesion:(long) sessionId{
    Session* session = [self.sessionMap objectForKey:[NSString stringWithFormat:@"%i",sessionId]];
    return session;
}

-(void) deleteSession:(long) sessionId{
    [self.sessionMap removeObjectForKey:[NSString stringWithFormat:@"%i",sessionId]];
}

-(State*) findStateById:(long) stateId sessionId:(long) sessionId{
    Session* session = [self querySesion:sessionId];
    for (State* state in session.states) {
        if(state.stateId==stateId){
            return state;
        }
    }
    return nil;
}

-(IState*) getState:(NSString*) sessionName stateName:(NSString*) stateName{
    NSMutableDictionary* stateMap = [self.sessionStateMap objectForKey:sessionName];
    return [stateMap objectForKey:stateName];
}

- (void)dealloc {
    [_sessionMap release];
    _sessionMap = nil;
    [_startStateMap release];
    _startStateMap = nil;
    [super dealloc];
}
@end
