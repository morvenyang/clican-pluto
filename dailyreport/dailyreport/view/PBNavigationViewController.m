//
//  PBNavigationViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-11-25.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "PBNavigationViewController.h"
#import "StyleSheet.h"
#import "AppDelegate.h"
@implementation PBNavigationViewController
@synthesize  backIndex = _backIndex;
-(id) initWithBrand:(NSString*) brand backIndex:(int)backIndex{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.backIndex = backIndex;
        self.index=-2;
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
    
    CGFloat width = SCREEN_WIDTH/3;
    CGFloat height = (SCREEN_HEIGHT-64)/3;
    [self.contentView addSubview:[self createImageViewFromColor:[StyleSheet colorFromHexString:@"#b3b3b3"] frame:CGRectMake(width, 0, 0.5, SCREEN_HEIGHT-64)]];
    
    [self.contentView addSubview:[self createImageViewFromColor:[StyleSheet colorFromHexString:@"#b3b3b3"] frame:CGRectMake(width*2, 0, 0.5, SCREEN_HEIGHT-64)]];
    [self.contentView addSubview:[self createImageViewFromColor:[StyleSheet colorFromHexString:@"#b3b3b3"] frame:CGRectMake(0, height, SCREEN_WIDTH, 0.5)]];
    [self.contentView addSubview:[self createImageViewFromColor:[StyleSheet colorFromHexString:@"#b3b3b3"] frame:CGRectMake(0, height*2, SCREEN_WIDTH, 0.5)]];
    NSArray* names = [NSArray arrayWithObjects:@"零售总览",@"关键指标",@"零售结构分析",@"店铺排名",@"商品排名",@"电商",@"拓展统计",@"未上传店铺", nil];
    int labelFont = [self getFont:16 ip6Offset:2 ip6pOffset:2];
    for(int i=0;i<names.count;i++){
        NSString* name = [names objectAtIndex:i];
        UIView* v = [[[UIView alloc] initWithFrame:CGRectMake(width*(i%3), (i/3)*height, width, height)] autorelease];
        UIButton* button =[UIButton buttonWithType:UIButtonTypeCustom];
        [button setTitle:[NSString stringWithFormat:@"%i",i+1] forState:UIControlStateNormal];
        UIImage* image =[UIImage imageNamed:[NSString stringWithFormat:@"大图标-%@",name]];
        [button setImage:image forState:UIControlStateNormal];
        [button addTarget:self action:@selector(onButtonClick:) forControlEvents:UIControlEventTouchUpInside];
        button.frame = CGRectMake((v.frame.size.width-image.size.width)/2, (v.frame.size.height-image.size.height)/3, image.size.width, image.size.height);
        
        UILabel* label = [self createLabel:name frame:CGRectMake(0, (v.frame.size.height-image.size.height)/3+image.size.height, v.frame.size.width,  (v.frame.size.height-image.size.height)*2/3) textColor:@"#000000" font:labelFont backgroundColor:nil textAlignment:ALIGN_CENTER];

        [v addSubview:button];
        [v addSubview:label];
        [self.contentView addSubview:v];
    }
}

-(void)onButtonClick:(id)sender{
    UIButton* button = (UIButton*)sender;
    int index = button.titleLabel.text.intValue;
    [[TTNavigator navigator] removeAllViewControllers];
    
    for(int i=1;i<=index;i++){
        NSString* url = [self getBackUrl:i];
        
        TTURLAction* action=[TTURLAction actionWithURLPath:url];
        [[TTNavigator navigator] openURLAction:
        [action applyAnimated:NO]];
    }
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
