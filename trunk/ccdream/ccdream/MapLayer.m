//
//  MapLayer.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "MapLayer.h"


@implementation MapLayer

@synthesize char1 = _char1;

+(CCScene *) scene{
    CCScene* scene = [CCScene node];
    MapLayer* layer = [MapLayer node];
    layer.char1=[[[Character alloc] initWithParentNode:layer spriteFile:@"trs-012.gif"] autorelease];
    [scene addChild:layer];
    return scene;
}

-(id) init{
    self = [super init];
    if(self!=nil){
        CCTMXTiledMap* tileMap = [CCTMXTiledMap tiledMapWithTMXFile:@"ccdream_map01.tmx"];
        [self addChild:tileMap z:-1 tag:1]; 
        CCTMXLayer* eventLayer = [tileMap layerNamed:@"GameEventLayer"]; 
        eventLayer.visible = NO;
    }
    return self;
}
@end
