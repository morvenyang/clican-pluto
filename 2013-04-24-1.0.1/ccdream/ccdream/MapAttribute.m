//
//  MapAttribute.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "MapAttribute.h"


@implementation MapAttribute

@synthesize playerBeginPosiArray = _playerBeginPosiArray;
@synthesize enemyBeginPosiArray = _enemyBeginPosiArray;

-(id) init { 
    self = [super init];
    if(self!=nil){
        self.playerBeginPosiArray = [CCArray array];
        self.enemyBeginPosiArray = [CCArray array];
    }
    return self;
}
+(id) mapAttributeWith:(CCTMXTiledMap*) tiledMap{
    MapAttribute* ma = [[[MapAttribute alloc] init] autorelease];
    
    //加载map属性
    NSString* playerBeginPosiStr= [tiledMap.properties objectForKey:@"playerBeginPosi"];
    NSString* enemyBeginPosiStr= [tiledMap.properties objectForKey:@"enemyBeginPosi"];
    
    NSArray* pArray = [playerBeginPosiStr componentsSeparatedByString:@","];
    NSArray* eArray = [enemyBeginPosiStr componentsSeparatedByString:@","];
    
    for(int i=0;i<[pArray count];i=i+2){
        NSString* xStr = [pArray objectAtIndex:i];
        NSString* yStr = [pArray objectAtIndex:i+1];
        Position* posi = [Position positionWithX:[xStr intValue] Y:[yStr intValue]];
        
        [ma.playerBeginPosiArray addObject:posi];
    }
    
    for(int i=0;i<[eArray count];i=i+2){
        NSString* xStr = [eArray objectAtIndex:i];
        NSString* yStr = [eArray objectAtIndex:i+1];
        Position* posi = [Position positionWithX:[xStr intValue] Y:[yStr intValue]];
        
        [ma.enemyBeginPosiArray addObject:posi];
    }
    return ma;
}

- (void)dealloc {
    [_playerBeginPosiArray release];
    _playerBeginPosiArray = nil;
	[_enemyBeginPosiArray release];
    _enemyBeginPosiArray = nil;
    [super dealloc];
}
@end
