//
//  XMLParserDelegate.m
//  ccdream
//
//  Created by wei zhang on 12-6-7.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "XMLParserDelegate.h"


@implementation XMLParserDelegate

@synthesize startState = _startState;
@synthesize currentParseState = _currentParseState;
@synthesize nextStatesMap = _nextStatesMap;
@synthesize nextCondStatesMap = _nextCondStatesMap;
@synthesize statesMap = _statesMap;

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict{
    BOOL stateElement = NO;
    
    if([elementName isEqualToString:@"start"]){
        stateElement = YES;
        self.currentParseState = [[[StartState alloc] init] autorelease];
    }else if([elementName isEqualToString:@"end"]){
        stateElement = YES;
        self.currentParseState = [[[EndState alloc] init] autorelease];
    }else if([elementName isEqualToString:@"state"]){
        stateElement = YES;
        NSString* className = [attributeDict objectForKey:@"class"];
        if(className!=nil&&[className length]>0){
            self.currentParseState = [[[NSClassFromString(className) alloc] init] autorelease];
        }else{
            self.currentParseState = [[[DefaultState alloc] init] autorelease];
        }
    }else if([elementName isEqualToString:@"task"]){
        stateElement = YES;
        NSString* className = [attributeDict objectForKey:@"class"];
        if(className!=nil&&[className length]>0){
            self.currentParseState = [[[NSClassFromString(className) alloc] init] autorelease];
        }else{
            self.currentParseState = [[[TaskState alloc] init] autorelease];
        }
    }else if([elementName isEqualToString:@"nextCondStates"]){
        
        NSString* expr = [attributeDict objectForKey:@"expr"];
        NSString* nextStates = [attributeDict objectForKey:@"nextStates"];
        NSMutableDictionary* dict = [self.nextCondStatesMap objectForKey:self.currentParseState.name];
        
        if(dict==nil){
            dict = [[[NSMutableDictionary alloc] init] autorelease];
            [self.nextCondStatesMap setValue:dict forKey:self.currentParseState.name];
        }
        
        [dict setValue:nextStates forKey:expr];
    }else if([elementName isEqualToString:@"stateListener"]){
        NSString* className = [attributeDict objectForKey:@"class"];
        id<StateListener> stateListener = [[[NSClassFromString(className) alloc] init] autorelease];
        [self.currentParseState.stateListeners addObject:stateListener];
    }else if([elementName isEqualToString:@"taskListener"]){
        NSString* className = [attributeDict objectForKey:@"class"];
        id<TaskListener> taskListener = [[[NSClassFromString(className) alloc] init] autorelease];
        if([self.currentParseState isKindOfClass:[TaskState class]]){
            TaskState* taskState = (TaskState*)self.currentParseState;
            [taskState.taskListeners addObject:taskListener];
        }else{
            CCLOGERROR(@"Parse a task listener but not under task state");
        }
    }
    
    if(stateElement){
        NSString* name = [attributeDict objectForKey:@"name"];
        self.currentParseState.name = name;
        NSString* value = [attributeDict objectForKey:@"value"];
        if(value!=nil&&[value length]>0){
            self.currentParseState.value = [value intValue];
        }
        NSString* propagation = [attributeDict objectForKey:@"propagation"];
        self.currentParseState.propagation = propagation;
        NSString* nextStates = [attributeDict objectForKey:@"nextStates"];
        if(nextStates!=nil&&[nextStates length]>0){
            [self.nextStatesMap setValue:nextStates forKey:name];
        }
        [self.statesMap setValue:self.currentParseState forKey:name];
    }
    
}

- (void)parserDidEndDocument:(NSXMLParser *)parser{
    for(IState* state in [self.statesMap allKeys]){
        if(state!=nil){
            //处理下个状态节点
            NSString* nextStates = [self.nextStatesMap objectForKey:state.name];
            if(nextStates!=nil&&[nextStates length]>0){
                NSArray* array = [nextStates componentsSeparatedByString:@","];
                for(int i=0;i<[array count];i++){
                    NSString* stateName = [array objectAtIndex:i];
                    IState* nextState = [self.statesMap objectForKey:stateName];
                    [state.nextStats addObject:nextState];
                }
            }
            
            //处理下个带条件状态节点
            
            NSMutableDictionary* nextCondStatesMap = [self.nextCondStatesMap objectForKey:state.name];
            
            if(nextCondStatesMap!=nil){
                for (NSString* expr in [nextCondStatesMap allKeys]) {
                    NSString* nextCondStates = [nextCondStatesMap valueForKey:expr]; 
                    NSArray* array = [nextCondStates componentsSeparatedByString:@","];
                    NSMutableArray* nextStateArray = [[[NSMutableArray alloc] init]autorelease];
                    for(int i=0;i<[array count];i++){
                        NSString* stateName = [array objectAtIndex:i];
                        IState* nextState = [self.statesMap objectForKey:stateName];
                        [nextStateArray addObject:nextState];
                    }
                    [state.nextCondStates setValue:nextStateArray forKey:expr];
                }
                
            }
        }else{
            break;
        }
    }
}
@end