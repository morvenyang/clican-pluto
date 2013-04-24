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

- (id) init{
    self = [super init];
    if(self!=nil){
        self.nextStatesMap = [[[NSMutableDictionary alloc] init] autorelease];
        self.nextCondStatesMap = [[[NSMutableDictionary alloc] init] autorelease];
        self.statesMap = [[[NSMutableDictionary alloc] init] autorelease];
    }
    return self;
}
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict{
    BOOL stateElement = NO;
    NSString* name = [attributeDict objectForKey:@"name"];
    CCLOG(@"parse name:%@",name);
    if([elementName isEqualToString:@"start"]){
        stateElement = YES;
        self.startState = [[[StartState alloc] init] autorelease];
        self.currentParseState = self.startState;
    }else if([elementName isEqualToString:@"end"]){
        stateElement = YES;
        self.currentParseState = [[[EndState alloc] init] autorelease];
    }else if([elementName isEqualToString:@"state"]){
        stateElement = YES;
        NSString* className = [attributeDict objectForKey:@"class"];
        if(className!=nil&&[className length]>0){
                self.currentParseState = [[[NSClassFromString(className) alloc] init] autorelease];
            if(self.currentParseState==nil){
                CCLOGERROR(@"The class [%@] not exist, we use DetaultState.",className);
                self.currentParseState = [[[DefaultState alloc] init] autorelease];
            }
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
        NSString* assignees = [attributeDict objectForKey:@"assignees"];
        ((TaskState*)self.currentParseState).assignees = assignees;
        NSString* taskName = [attributeDict objectForKey:@"taskName"];
        ((TaskState*)self.currentParseState).taskName = taskName;
        NSString* taskType = [attributeDict objectForKey:@"taskType"];
        ((TaskState*)self.currentParseState).taskType = taskType;
    }else if([elementName isEqualToString:@"nextCondStates"]){
        CCLOG(@"current parse state name:%@",self.currentParseState.name);
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
        
        self.currentParseState.name = name;
        NSString* value = [attributeDict objectForKey:@"value"];
        if(value!=nil&&[value length]>0){
            self.currentParseState.value = [value intValue];
        }
        NSString* nextStates = [attributeDict objectForKey:@"nextStates"];
        if(nextStates!=nil&&[nextStates length]>0){
            [self.nextStatesMap setValue:nextStates forKey:name];
        }
        [self.statesMap setValue:self.currentParseState forKey:name];
    }
    
}

- (void)parserDidEndDocument:(NSXMLParser *)parser{
    for(NSString* name in [self.statesMap allKeys]){
        IState* state = [self.statesMap valueForKey:name];
        
        //处理下个状态节点
        CCLOG(@"state name=%@",name);
        NSString* nextStates = [self.nextStatesMap objectForKey:state.name];
        if(nextStates!=nil&&[nextStates length]>0){
            NSArray* array = [nextStates componentsSeparatedByString:@","];
            for(int i=0;i<[array count];i++){
                NSString* stateName = [array objectAtIndex:i];
                IState* nextState = [self.statesMap objectForKey:stateName];
                if(nextState==nil){
                    CCLOGERROR(@"The state[%@] doesn't exist",stateName);
                }
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
        
    }
}
@end