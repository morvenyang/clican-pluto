//
//  PBNavigationViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-11-25.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "PBNavigationViewController.h"

@implementation PBNavigationViewController
@synthesize  backIndex = _backIndex;
-(id) initWithBrand:(NSString*) brand backIndex:(int)backIndex{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.backIndex = backIndex;
    }
    return self;
}

-(void) backAction{
    
    NSString* backUrl = [self getBackUrl:self.backIndex];
    TTOpenURL(backUrl);
}


- (void)loadView
{
    [super loadView];
    for(int i=0;i<8;i++){
        UIButton* button =[UIButton buttonWithType:UIButtonTypeCustom];
        [button setTitle:[NSString stringWithFormat:@"%i",(i+1)] forState:UIControlStateNormal];
        [button setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [button addTarget:self action:@selector(onButtonClick:) forControlEvents:UIControlEventTouchUpInside];
        button.frame = CGRectMake(SCREEN_WIDTH*(i%3)/3, (i/3)*SCREEN_HEIGHT/3, SCREEN_WIDTH/3, SCREEN_HEIGHT/3);
        NSLog(@"%f %f %f %f",button.frame.origin.x,button.frame.origin.y,button.frame.size.width,button.frame.size.height);
        [self.contentView addSubview:button];
    }
}

-(void)onButtonClick:(id)sender{
    UIButton* button = (UIButton*)sender;
    int index = button.titleLabel.text.intValue;
    NSString* backUrl = [self getBackUrl:index];
    [(SwitchViewController*)TTOpenURL(backUrl) hideShareView];
}

-(NSString*) getBackUrl:(int)index{
    NSString* backUrl = @"";
    if(index==1){
        backUrl = [NSString stringWithFormat:@"peacebird://brand/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }else if(index==2){
        backUrl = [NSString stringWithFormat:@"peacebird://kpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }else if(index==3){
        backUrl = [NSString stringWithFormat:@"peacebird://retail/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }else if(index==4){
        backUrl = [NSString stringWithFormat:@"peacebird://storeRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }else if(index==5){
        backUrl = [NSString stringWithFormat:@"peacebird://goodRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }else if(index==6){
        backUrl = [NSString stringWithFormat:@"peacebird://b2cKpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }else if(index==7){
        backUrl = [NSString stringWithFormat:@"peacebird://storeSum/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }else if(index==8){
        backUrl = [NSString stringWithFormat:@"peacebird://noRetails/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }
    return backUrl;
}
-(void)dealloc{
    [super dealloc];
}

@end
