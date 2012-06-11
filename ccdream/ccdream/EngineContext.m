//
//  EngineContext.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "EngineContext.h"
#import "XMLParserDelegate.h"
#import "WorkflowConstants.h"
@implementation EngineContext

@synthesize sessionMap = _sessionMap;
@synthesize startStateMap = _startStateMap;
@synthesize sessionStateMap = _sessionStateMap;

static EngineContext *sharedEngineContext = nil;

-(id) init {
    self = [super init];
    if(self){
        _atomicLong = [[NSDate date] timeIntervalSince1970];
        _sessionMap = [[[NSMutableDictionary alloc] init] autorelease];
        _startStateMap = [[[NSMutableDictionary alloc] init] autorelease];
        _sessionStateMap = [[[NSMutableDictionary alloc] init] autorelease];
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
        CCLOG(@"xml file name=%@",[name stringByAppendingString:@".xml"]);

        NSBundle* bundle = [NSBundle mainBundle];
        NSString* path = [bundle pathForResource:name ofType:@"xml"];
        CCLOG(@"xml path=%@",path);
        NSURL* url = [NSURL fileURLWithPath:path];
        NSXMLParser *xmlParser = [[NSXMLParser alloc] initWithContentsOfURL:url];
        //Initialize the delegate.
        XMLParserDelegate* parser = [[XMLParserDelegate alloc] init];
        //Set delegate
        [xmlParser setDelegate:parser];
        BOOL success = [xmlParser parse];
        if(success){
            CCLOG(@"parse %@.xml successfully",name);
            startState = parser.startState;
            CCLOG(@"start state[%@] for session[%@]",startState.name,name);
            [self.startStateMap setValue:startState forKey:name];
            [self.sessionStateMap setValue:parser.statesMap forKey:name];
            return startState;
        }else{
            CCLOGERROR(@"parse %@.xml failure",name);
            return nil;
        }
    }
}




-(Session*) newSession:(NSString*) name forSponsor:(NSString*) sponsor{
    StartState* startState = [self loadSession:name];
    if(startState!=nil){
        Session* session = [[[Session alloc] init] autorelease];
        session.sessionId = [self getAndAddAtomicLong];
        session.name = name;
        NSString* sessionIdStr = [NSString stringWithFormat:@"%ld",session.sessionId];
        CCLOG(@"new session with id=%@",sessionIdStr);
        session.sponsor = sponsor;
        [_sessionMap setValue:session forKey:sessionIdStr];
        [startState onStart:session istate:nil event:nil];
        return session;
    }else{
        return nil;
    }
    
}

-(Session*) querySesion:(long) sessionId{
    NSString* sessionIdStr = [NSString stringWithFormat:@"%ld",sessionId];
    CCLOG(@"query session with id=%@",sessionIdStr);
    CCLOG(@"session map count=%i",[self.sessionMap count]);
    Session* session = [self.sessionMap objectForKey:sessionIdStr];
    return session;
}

-(void) deleteSession:(long) sessionId{
    [self.sessionMap removeObjectForKey:[NSString stringWithFormat:@"%ld",sessionId]];
}

-(State*) findActiveStateBySessionId:(long) sessionId{
    Session* session = [self querySesion:sessionId];
    for (State* state in session.states) {
        if([state.status isEqualToString:STATUS_ACTIVE]){
            return state;
        }
    }
    return nil;
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
    CCLOG(@"session name:%@,state name:%@",sessionName,stateName);
    CCLOG(@"session state map count:%i",[self.sessionStateMap count]);
    NSMutableDictionary* stateMap = [self.sessionStateMap objectForKey:sessionName];
    CCLOG(@"state map count:%i",[stateMap count]);
    return [stateMap objectForKey:stateName];
}

- (void)dealloc {
    [_sessionMap release];
    _sessionMap = nil;
    [_startStateMap release];
    _startStateMap = nil;
    [_sessionStateMap release];
    _sessionStateMap = nil;
    [super dealloc];
}
@end
