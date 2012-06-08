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

-(StartState*) loadSession:(NSString*) name {
    StartState* startState = [self.sessionMap objectForKey:name];
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
            [self.sessionMap setValue:startState forKey:name];
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
        session.sponsor = sponsor;
        
        return session;
    }else{
        return nil;
    }
    
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
