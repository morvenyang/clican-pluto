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
    NSError* error;
    NSString *path = [[NSBundle mainBundle] pathForResource:name ofType:@"xml"];
    NSString *fileContent = [NSString stringWithContentsOfFile:path encoding:NSUTF8StringEncoding error:&error];
    GDataXMLDocument *document = [[GDataXMLDocument alloc] initWithXMLString:fileContent options:1 error:&error];
    GDataXMLElement *rootElement = [document rootElement];
    CCLOG(@"******rootElement******\n%@",rootElement);
    
    
}

-(void) parseDocument:(GDataXMLElement*) element stateMap:(NSMutableDictionary*) stateMap, istate:(IState*) istate {    

    for(int i=0;i<element.childCount;i++){
        GDataXMLNode* node = [element childAtIndex:i];
        if([node isKindOfClass:GDataXMLElement]){
            GDataXMLElement* e = (GDataXMLElement*)node;
            NSString* elementName = [e name];
            BOOL stateElement = YES;
            if([elementName isEqualToString:@"start"]){
                istate = [[[StartState alloc] init] autorelase];
            }else if([elementName isEqualToString:@"end"]){
                istate = [[[EndState alloc] init] autorelase];
            }else if([elementName isEqualToString:@"state"]){
                NSString* clsssName = [[e attributeForName:@"class"] stringValue];
                if(className!=nil&&[className length]>){
                    
                }
            }else if([elementName isEqualToString:@"task"]){
                
            }
        }else{
            
        }
    }
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
