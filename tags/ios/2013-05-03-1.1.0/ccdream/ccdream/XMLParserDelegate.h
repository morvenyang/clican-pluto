//
//  XMLParserDelegate.h
//  ccdream
//
//  Created by wei zhang on 12-6-7.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "IState.h"
#import "StartState.h"
#import "EndState.h"
#import "DefaultState.h"
#import "TaskState.h"
#import "Listener.h"
#import "IState.h"

@class StartState;
@class IState;

@interface XMLParserDelegate : NSObject<NSXMLParserDelegate> {
    StartState* _startState;
    IState* _currentParseState;
    NSMutableDictionary* _nextStatesMap;
    NSMutableDictionary* _nextCondStatesMap;
    NSMutableDictionary* _statesMap;
}

@property (nonatomic,retain) StartState* startState;
@property (nonatomic,retain) IState* currentParseState;
@property (nonatomic,retain) NSMutableDictionary* nextStatesMap;
@property (nonatomic,retain) NSMutableDictionary* nextCondStatesMap;
@property (nonatomic,retain) NSMutableDictionary* statesMap;
@end
