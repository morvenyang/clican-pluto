//
//  BrandViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "BrandViewController.h"
#import "StyleSheet.h"

@interface BrandViewController ()

@end

@implementation BrandViewController

@synthesize brand = _brand;
@synthesize brandModel = _brandModel;

-(id) initWithBrand:(NSString*) brand{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.brandModel = [[[BrandModel alloc] initWithBrand:self.brand delegate:self] autorelease];
    }
    return self;
}
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
- (void)loadView
{
    
    [super loadView];
    [self.brandModel load:TTURLRequestCachePolicyNone more:NO];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    
}

- (void) brandDidFinishLoad:(Brand*) brand channels:(NSArray*) channels weeks:(NSArray*) weeks{
     NSLog(@"%@",@"加载Brand数据成功");
}

- (void) brandDidStartLoad:(NSString*) brand{
    NSLog(@"%@",@"开始加载Brand数据");
}

- (void) brand:(NSString*)brand didFailLoadWithError:(NSError*)error{
    if([error code]==-1004||[error code]==-1001){
        TTAlert(@"请检查网络链接");
    }else{
        TTAlert([error localizedDescription]);
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc
{
    
    TT_RELEASE_SAFELY(_brand);
    TT_RELEASE_SAFELY(_brandModel);
    [super dealloc];
}
@end
